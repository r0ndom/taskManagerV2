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
import ua.pb.task.manager.repository.session.SessionStorage;
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

    @Value("${login.failed.url}")
    private String LOGIN_FAILED;

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
        Person profile = getPlusProfile(credential);
        User user = User.newBuilder()
                .setEmails(getStringEmails(profile.getEmails()))
                .addRole(Role.ROLE_GUEST)
                .build();
        userRepository.store(user);
        createSession(user, credential);
    }

    public void auth() throws IOException {
        Credential credential = storage.getNewInstance();
        Person profile = getPlusProfile(credential);
        String email = getMainEmail(profile.getEmails());
        User user = userRepository.findByEmail(email);
        createSession(user, credential);
    }

    private Person getPlusProfile(Credential credential) throws IOException {
        Plus plus = getPlusService(credential);
        return plus.people().get(DEFAULT_USER).execute();
    }


    private Plus getPlusService(Credential credential) {
        return new Plus.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private void createSession(User user, Credential credential) throws IOException {
        storage.store(user.getId(), credential);
        String sessionKey = sessionStorage.storeObject(user.getId());
        requestUtil.updateOrCreateSessionKey(request, response, User.USER_KEY_NAME_COOKIE, sessionKey, "/");
        response.sendRedirect(REDIRECT_URL);
    }

    private List<String> getStringEmails(List<Person.Emails> emails) {
        List<String> result = new ArrayList<>();
        for (Person.Emails email : emails) {
            result.add(email.getValue());
        }
        return result;
    }

    private String getMainEmail(List<Person.Emails> emails) {
        return emails.get(0).getValue();
    }

}
