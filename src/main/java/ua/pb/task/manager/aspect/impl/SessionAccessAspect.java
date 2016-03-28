package ua.pb.task.manager.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.pb.task.manager.aspect.ApplyOrder;
import ua.pb.task.manager.aspect.annotation.SessionAccess;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.repository.session.SessionStorage;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Aspect
@Order(ApplyOrder.AUTH)
public class SessionAccessAspect {
    private static final Logger LOG = LoggerFactory.getLogger(SessionAccessAspect.class);

    @Autowired
    private SessionStorage<Long> sessionStorage;

    @Autowired
    private RequestUtil requestUtil;

//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private HttpServletResponse response;

    @Value("${login.failed.url}")
    private String LOGIN_FAILED;


    @Pointcut("within(@ua.pb.task.manager.aspect.annotation.SessionAccess *)")
    public void sessionAccess() {
    }



    @Around("sessionAccess()")
    public void checkAccess(ProceedingJoinPoint call) throws Throwable {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LOG.info("map by req(SessionAccess) - {}", request.getParameterMap());

        LOG.info("SESSION ASPECT");

        Cookie cookie = requestUtil.getLastSessionCookieByKey(request, User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(request, cookie);
        if (key != null) {
            Long id = sessionStorage.getObject(key);
            if (id == null) {
                requestUtil.deleteAllCookieByKey(request, User.USER_KEY_NAME_COOKIE);
                response.addHeader("SESSION", "TIMEOUT");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                sessionStorage.storeObject(key, id);
                call.proceed();
            }
        } else {
            response.addHeader("SESSION", "UNAUTHORIZED");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}