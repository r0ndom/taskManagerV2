package ua.pb.task.manager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;
import ua.pb.statements.model.domain.*;
import ua.pb.statements.service.session.SessionStorage;
import ua.pb.statements.util.RequestUtil;
import ua.pb.statements.util.XmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Aspect
public class ExceptionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);

    @Autowired
    @Qualifier("sessionStorage")
    private SessionStorage<P24User> sessionStorage;

    @Autowired
    private XmlUtils xmlUtils;

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
        } catch (BadSqlGrammarException e) {
            String message = e.getRootCause().getMessage();
            getErrorInfo(message, e);
        } catch (SQLException e) {
            String message = e.getCause().getMessage();
            getErrorInfo(message, e);
        } catch (Exception e) {
            String message = e.getMessage();
            getErrorInfo(message, e);
        }
    }

    private void getErrorInfo(String message, Exception e) {
        String uid = requestUtil.send2ErrorService(req, e);
        String exception = e.getClass().getSimpleName();
        printError(exception, message, uid);
        e.printStackTrace();
    }

    private void printError(String exception, String message, String uid) {
        P24User user = getUserByCookie();

        LOG.error("User data:" + (user != null ? user.toString() : "user = null"));

        String errorXml = xmlUtils.getErrorXml(new Error(user, exception, message, uid));

        LOG.error("Error:" + exception + ":" + message + ":" + uid);
        requestUtil.printContent(resp, errorXml);
    }

    private P24User getUserByCookie() {
        P24User user = null;
        Cookie sessionCookie = requestUtil.getLastSessionCookieByKey(req, P24User.USER_KEY_NAME_COOKIE);
        String key = requestUtil.getSessionKeyByCookieOrAttribute(req, sessionCookie);
        if (key != null) {
            user = sessionStorage.getObject(key);
        }
        return user;
    }
}