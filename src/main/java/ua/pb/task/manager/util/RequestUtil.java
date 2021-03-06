package ua.pb.task.manager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.pb.task.manager.model.*;
import ua.pb.task.manager.repository.session.SessionStorage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Component
public class RequestUtil {

    @Autowired
    private SessionStorage<UserSession> sessionStorage;

    private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);

    @Value("${error.url}")
    private String ERROR_URL;

    public Cookie getLastSessionCookieByKey(HttpServletRequest req, String cookieName) {
        LOG.debug("cookieName {}: ", cookieName);
        Cookie[] cookies = req.getCookies();
        if (cookies == null)
            return null;
        Cookie sessionCookie = null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                sessionCookie = cookie;
            }
        }

        LOG.debug("getLastSessionCookieByKey-sessionCookie {}: ", sessionCookie == null ? "NULL" : sessionCookie.getValue());
        return sessionCookie;
    }

    public void deleteSessionKey(HttpServletRequest req, String sessionKeyName) {
        LOG.debug("userSessionKey {}: ", sessionKeyName);

        Cookie[] cookies = req.getCookies();
        if (cookies == null){
            LOG.debug("sessionKeyName == null");
            return;
        }
        for (Cookie cookie : cookies) {
            if (sessionKeyName.equals(cookie.getName())) {
                LOG.debug("sessionKeyName for setMaxAge(-1)");
                cookie.setMaxAge(-1);
                return;
            }
        }
    }

    public void deleteAllCookieByKey(HttpServletRequest req, String deleteCookieKey) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (deleteCookieKey.equals(cookie.getName())) {
                cookie.setMaxAge(0);
            }
        }
    }

    public void updateOrCreateSessionKey(HttpServletRequest req, HttpServletResponse resp, String sessionKeyName, String sessionValue, String cookiePath) {
        deleteSessionKey(req, sessionKeyName);
        LOG.debug("deleteSessionKey");
        Cookie cookie = new Cookie(sessionKeyName, sessionValue);
        cookie.setPath(cookiePath);
        resp.addCookie(cookie);
        LOG.debug("new Cookie: {}", cookie.getValue());
    }

    public String getSessionKeyByCookieOrAttribute(HttpServletRequest req, Cookie sessionCookie) {
        String sessionKey = String.valueOf(req.getAttribute(User.USER_KEY_NAME_COOKIE));
        if (sessionKey == null || sessionKey.trim().isEmpty() || "null".equals(sessionKey)) {
            sessionKey = (sessionCookie != null) ? sessionCookie.getValue() : null;
        }
        return sessionKey;
    }

    public String getErrorUrl(ua.pb.task.manager.model.Error error) {
        StringBuilder builder = new StringBuilder();
        builder.append(ERROR_URL);
        builder.append("?").append("ref").append(error.getRef());
        builder.append("&").append("type").append(error.getType());
        builder.append("&").append("message").append(error.getMessage());
        return builder.toString();
    }

    public UserSession getUserSession(HttpServletRequest request) {
        Cookie cookie = getLastSessionCookieByKey(request, User.USER_KEY_NAME_COOKIE);
        String key = getSessionKeyByCookieOrAttribute(request, cookie);
        return sessionStorage.getObject(key);
    }
}
