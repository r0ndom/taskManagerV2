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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.pb.task.manager.aspect.ApplyOrder;
import ua.pb.task.manager.model.User;
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
    private SessionStorage<Long> sessionStorage;

//    @Autowired
//    private HttpServletRequest req;
//
//    @Autowired
//    private HttpServletResponse resp;

    @Autowired
    private RequestUtil requestUtil;

    @Value("${error.url}")
    private String REDIRECT_URL;

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

    private void sendError(String message, Exception e) throws IOException {
        Long id = getUserByCookie();

        LOG.error("User data:" + (id != null ? id : "user = null"));

        String uid = UUID.randomUUID().toString();
        String exception = e.getClass().getSimpleName();

        LOG.error("Error:" + exception + ":" + message + ":" + uid);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.sendRedirect(REDIRECT_URL);

    }

    private Long getUserByCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie sessionCookie = requestUtil.getLastSessionCookieByKey(request, User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(request, sessionCookie);
        return (key != null) ? sessionStorage.getObject(key) : null;
    }
}