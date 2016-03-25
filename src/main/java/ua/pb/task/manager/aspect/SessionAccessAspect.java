package ua.pb.task.manager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import ua.pb.statements.aspect.annotation.SessionAccessControl;
import ua.pb.statements.aspect.util.AspectApplyOrder;
import ua.pb.statements.model.domain.P24User;
import ua.pb.statements.resources.ResourcesLoader;
import ua.pb.statements.service.session.SessionStorage;
import ua.pb.statements.util.RequestUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Aspect
@Order(AspectApplyOrder.SESSION_ACCESS_ASPECT)
public class SessionAccessAspect {
    private static final Logger LOG = LoggerFactory.getLogger(SessionAccessAspect.class);

    private static final Marker ACCESS_MARKER = MarkerFactory.getMarker("ACCESS");

    private static final String AJAX_HEADER = "X-Requested-With";
    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

    @Autowired
    @Qualifier("sessionStorage")
    private SessionStorage<P24User> sessionStorage;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private HttpServletRequest req;
    @Autowired
    private HttpServletResponse resp;

    @Autowired
    @Qualifier("customResourceLoader")
    public ResourcesLoader customResourceLoader;

    @Pointcut("within(@ua.pb.statements.aspect.annotation.SessionAccessControl *)")
    public void sessionAccess() {
    }


    /**
     * Check user access with caching {@see SessionContextHolder}
     *
     * @param call
     * @throws Throwable
     */
    @Around("sessionAccess()"
    )
    public void checkAccess(ProceedingJoinPoint call) throws Throwable {
        LOG.info("map by req(SessionAccess) - {}", req.getParameterMap());
        LOG.trace("ASPECT :: SessionAccessAspect - checkAccess ::");
        Cookie sessionCookie = requestUtil.getLastSessionCookieByKey(req, P24User.USER_KEY_NAME_COOKIE);
        String key = getSessionKeyByCookieOrAttribute(sessionCookie);

        LOG.info("SESSION ASPECT");

        SessionAccessControl sessionAccessAnnotation = getSessionAccessAnnotation(call);
        if (key != null) {
            LOG.debug("key != null SessionAccessAspect ");

            P24User user = sessionStorage.getObject(key);
            LOG.trace(ACCESS_MARKER, "ASPECT :: Call: {}", call.toShortString());
            //if session storage auto delete expired object
            if (user == null) {
                LOG.info("user = null SessionAccessAspect ");

                String sessionExpiredPage = formRedirectUrl(sessionAccessAnnotation);
                LOG.debug("Session is expired, redirect to {}", sessionExpiredPage);
                requestUtil.deleteAllCookieByKey(req, P24User.USER_KEY_NAME_COOKIE);
                resp.addHeader("SESSION", "TIMEOUT");
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                MDC.put("client", String.valueOf(user.getEkbIdFiz()));
                sessionStorage.storeObject(key, user, user.getSessionMaxTime());
                //For provide standard way for receiving user objects and better SL
                LOG.trace(ACCESS_MARKER, "Process request to {}", call.toShortString());
                call.proceed();
            }
        } else {
            String createSessionPage = createSessionUrl(sessionAccessAnnotation);
            LOG.debug("ASPECT :: Request without session key, redirect to {}, setup header value of response(name = SESSION, value = UNAUTHORIZED)", createSessionPage);

            if (AJAX_HEADER_VALUE.equalsIgnoreCase(req.getHeader(AJAX_HEADER))) {
                resp.addHeader("SESSION", "UNAUTHORIZED");
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                resp.addHeader("SESSION", "UNAUTHORIZED");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType(MediaType.TEXT_HTML_VALUE);

                PrintWriter out = resp.getWriter();
                String content = new String(Files.readAllBytes(Paths.get(customResourceLoader.getResources()[2].getFile().getPath())));
                out.println(content);
            }
        }
    }

    private String getSessionKeyByCookieOrAttribute(Cookie sessionCookie) {
        LOG.debug("getSessionKeyByCookieOrAttribute - sessionCookie {}: ", sessionCookie);

        //maybe need check argument for this hook?
        String sessionKey = String.valueOf(req.getAttribute(P24User.USER_KEY_NAME_COOKIE));
        LOG.debug("getSessionKeyByCookieOrAttribute - sessionKey {}: ", sessionKey);
        if (sessionKey == null || sessionKey.trim().isEmpty() || "null".equals(sessionKey)) {
            sessionKey = (sessionCookie != null) ? sessionCookie.getValue() : null;
            LOG.debug("getSessionKeyByCookieOrAttribute - sessionKey == null {}: ", sessionKey);
        }
        return sessionKey;
    }

    private String formRedirectUrl(SessionAccessControl sessionAccessControlAnnotation) {
        String encodedUri = null;
        try {
            encodedUri = URLEncoder.encode(req.getRequestURI(), "UTF-8");
        } catch (Exception e) {
            LOG.error("URLEncoder throws exception when encode session expired redirect url:", e);
        }
        return sessionAccessControlAnnotation.sessionExpiredUrl() + "?redirectUrl=" + encodedUri;
    }

    private String createSessionUrl(SessionAccessControl sessionAccessControl) {
        String encodedUri = null;
        try {
            encodedUri = URLEncoder.encode(req.getRequestURI(), "UTF-8");
        } catch (Exception e) {
            LOG.error("URLEncoder throws exception when encode create session redirect url:", e);
        }
        return sessionAccessControl.createSessionUrl() + "?redirectUrl=" + encodedUri;
    }

    SessionAccessControl getSessionAccessAnnotation(ProceedingJoinPoint call) {
        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(SessionAccessControl.class);
    }
}