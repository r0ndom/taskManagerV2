package ua.pb.task.manager.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.repository.UserRepository;
import ua.pb.task.manager.model.Role;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.service.session.SessionStorage;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mednikov on 09.03.2016.
 */
@Service
public class AuthService {

    @Value("${google.application.name}")
    private String APPLICATION_NAME;

    @Value("${google.plus.default.user}")
    private String DEFAULT_USER;

    @Value("${default.host.url}")
    private String REDIRECT_URL;

    @Autowired
    private JsonFactory JSON_FACTORY;

    @Autowired
    private HttpTransport HTTP_TRANSPORT;

    @Autowired
    private GoogleCredentialStorage storage;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionStorage<Long> sessionStorage;

    public void register() throws IOException {
        Credential credential = storage.getNewInstance();
        Plus plus = getPlusService(credential);
        Person profile = plus.people().get(DEFAULT_USER).execute();
        User user = User.newBuilder()
                .setEmails(getStringEmails(profile.getEmails()))
                .addRole(Role.ROLE_GUEST)
                .build();
        storage.store(user.getId(), credential);
        userRepository.store(user);
        String sessionKey = sessionStorage.storeObject(user.getId());
        requestUtil.updateOrCreateSessionKey(request, response, User.USER_KEY_NAME_COOKIE, sessionKey, "/");
        response.sendRedirect(REDIRECT_URL);
    }

    public void auth() {
        Cookie cookie = requestUtil.getLastSessionCookieByKey(request, User.USER_KEY_NAME_COOKIE);
        String key = getSessionKeyByCookieOrAttribute(cookie);
        if (key != null) {
            Long id = sessionStorage.getObject(key);
            if (id == null) {
                requestUtil.deleteAllCookieByKey(request, User.USER_KEY_NAME_COOKIE);
                response.addHeader("SESSION", "TIMEOUT");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                sessionStorage.storeObject(key, id);
            }
        } else {
            response.addHeader("SESSION", "UNAUTHORIZED");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


    private Plus getPlusService(Credential credential) {
        return new Plus.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private String getSessionKeyByCookieOrAttribute(Cookie sessionCookie) {
        String sessionKey = String.valueOf(request.getAttribute(User.USER_KEY_NAME_COOKIE));
        if (sessionKey == null || sessionKey.trim().isEmpty() || "null".equals(sessionKey)) {
            sessionKey = (sessionCookie != null) ? sessionCookie.getValue() : null;
        }
        return sessionKey;
    }

    private List<String> getStringEmails(List<Person.Emails> emails) {
        List<String> result = new ArrayList<>();
        for (Person.Emails email : emails) {
            result.add(email.getValue());
        }
        return result;
    }

}
