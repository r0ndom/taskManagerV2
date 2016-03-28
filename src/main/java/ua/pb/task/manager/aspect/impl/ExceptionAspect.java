package ua.pb.task.manager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.repository.session.SessionStorage;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Aspect
public class ExceptionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);

    @Autowired
    private SessionStorage<Long> sessionStorage;

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
    public void logAfterThrowing(ProceedingJoinPoint pjp) throws Throwable {
        try {
            pjp.proceed();
        } catch (Exception e) {
            String message = e.getMessage();
            sendError(message, e);
            e.printStackTrace();
        }
    }

    private void sendError(String message, Exception e) {
        Long id = getUserByCookie();

        LOG.error("User data:" + (id != null ? id : "user = null"));

        String uid = UUID.randomUUID().toString();
        String exception = e.getClass().getSimpleName();

        LOG.error("Error:" + exception + ":" + message + ":" + uid);

        resp.sendRedirect();

    }

    private Long getUserByCookie() {
        Cookie sessionCookie = requestUtil.getLastSessionCookieByKey(req, User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(req, sessionCookie);
        return (key != null) ? sessionStorage.getObject(key) : null;
    }
}