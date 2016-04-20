package ua.pb.task.manager.util;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope("singleton")
public class MessageUtils {
 
    public String getMessage(String key) {
 
        try {
            ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
            bean.setBasename("messages");
            bean.setDefaultEncoding("UTF-8");
            return bean.getMessage(key, null, Locale.getDefault());
        }
        catch (Exception e) {
            return "Unresolved key: " + key;
        }
    }
}