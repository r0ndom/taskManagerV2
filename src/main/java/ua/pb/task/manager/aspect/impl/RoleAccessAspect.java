package ua.pb.task.manager.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
import java.util.UUID;

/**
 * Created by Mednikov on 29.03.2016.
 */
@Aspect
@Order(ApplyOrder.CHECK_ROLE)
public class RoleAccessAspect {
    @Autowired
    private SessionStorage<UserSession> sessionStorage;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Value("${login.failed.url}")
    private String LOGIN_FAILED;


    @Pointcut("within(@ua.pb.task.manager.aspect.annotation.AdminAccess *)")
    public void sessionAccess() {
    }


    @Around("sessionAccess()")
    public Object checkAccess(ProceedingJoinPoint call) throws Throwable {
        Object result = null;

        Cookie cookie = requestUtil.getLastSessionCookieByKey(request, User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(request, cookie);
        UserSession session = sessionStorage.getObject(key);
        if (session.isAdmin()) {
            result = call.proceed();
        } else {
            //TODO move it to properties
            final String FORBIDDEN_ROLE = "FORBIDDEN_ROLE";
            Error error = new Error(UUID.randomUUID().toString(), FORBIDDEN_ROLE, "");
            String errorUrl = requestUtil.getErrorUrl(error);
            response.addHeader("SESSION", "FORBIDDEN_ROLE");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect(errorUrl);
        }
        return result;
    }
}
