package ua.pb.task.manager.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;

public class AppInitializer extends  AbstractAnnotationConfigDispatcherServletInitializer {

        @Override
        protected Class<?>[] getRootConfigClasses() {
            return new Class[]{AppConfig.class};
        }

        @Override
        protected Class<?>[] getServletConfigClasses() {
            return new Class<?>[]{WebConfig.class, AspectConfig.class};
        }

        @Override
        protected String[] getServletMappings() {
            return new String[]{"/"};
        }

        @Override
        protected Filter[] getServletFilters() {
            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setEncoding("UTF-8");
            characterEncodingFilter.setForceEncoding(true);
            return new Filter[] { characterEncodingFilter };
        }

}