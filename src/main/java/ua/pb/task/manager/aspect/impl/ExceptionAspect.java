package ua.pb.task.manager.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import ua.pb.task.manager.aspect.ApplyOrder;
import ua.pb.task.manager.model.*;
import ua.pb.task.manager.model.Error;
import ua.pb.task.manager.repository.session.SessionStorage;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Aspect
@Order(ApplyOrder.EXCEPTION)
public class ExceptionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);

    @Autowired
    private SessionStorage<UserSession> sessionStorage;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    private HttpServletResponse resp;

    @Autowired
    private RequestUtil requestUtil;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controller() {
    }

    @Around("controller()")
    public Object logAfterThrowing(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            String message = e.getMessage();
            sendError(message, e);
            e.printStackTrace();
        }
        return result;
    }

    private void sendError(String message, Exception e) throws IOException {
        UserSession session = getUserByCookie();

        LOG.error("User data:" + (session != null ? session : "user = null"));

        String uid = UUID.randomUUID().toString();
        String exception = e.getClass().getSimpleName();

        Error error = new Error(uid, exception, message);

        LOG.error("Error: {}", error);
        //TODO undrestand how to redirect to error service
        //how to handle from js
        //resp.sendRedirect(requestUtil.getErrorUrl(error));

    }

    private UserSession getUserByCookie() {
        Cookie sessionCookie = requestUtil.getLastSessionCookieByKey(req, User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(req, sessionCookie);
        return (key != null) ? sessionStorage.getObject(key) : null;
    }
}