package ua.pb.task.manager.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
* Created by Mednikov on 26.02.2016.
*/
@Configuration
@ComponentScan(basePackages = {"ua.pb.task.manager.repository", "ua.pb.task.manager.form",
        "ua.pb.task.manager.service"})
@Import({ActivitiConfig.class, DataSourceConfig.class, GoogleConfig.class})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
