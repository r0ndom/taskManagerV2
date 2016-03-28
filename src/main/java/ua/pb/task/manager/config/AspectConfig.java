package ua.pb.task.manager.config;

import org.springframework.context.annotation.*;
import ua.pb.task.manager.aspect.impl.ExceptionAspect;
import ua.pb.task.manager.aspect.impl.SessionAccessAspect;

/**
 * Created by Mednikov on 28.03.2016.
 */
@Configuration
@ComponentScan("ua.pb.task.manager.aspect")
@EnableAspectJAutoProxy
@PropertySource("classpath:auth.properties")
public class AspectConfig {

    @Bean
    public SessionAccessAspect sessionAccessAspect() {
        return new SessionAccessAspect();
    }

    @Bean
    public ExceptionAspect exceptionAspect() {
        return new ExceptionAspect();
    }
}
