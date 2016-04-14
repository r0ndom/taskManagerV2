package ua.pb.task.manager.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
* Created by Mednikov on 26.02.2016.
*/
@Configuration
@PropertySource("classpath:db.properties")
@MapperScan("ua.pb.task.manager.repository.mapper")
@EnableTransactionManagement
public class DataSourceConfig {

    public static final String REMOTE_HOST = System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST");
    public static final String REMOTE_USERNAME = System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME");
    public static final String REMOTE_PASSWORD = System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD");
    public static final String REMOTE_PORT = System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT");

    @Autowired
    private Environment env;
    @Autowired
    private ApplicationContext context;

    private Properties connectionProperties;

    public DataSourceConfig() throws IOException {
        connectionProperties = new Properties();

        //TODO need to find more readable way
        connectionProperties.load(new FileInputStream(getClass().getResource("/db.properties").getFile()));

        if (REMOTE_HOST != null) {
            connectionProperties.put("db.url", getRemoteUrl());
            connectionProperties.put("db.username", REMOTE_USERNAME);
            connectionProperties.put("db.password", REMOTE_PASSWORD);
        }
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(connectionProperties.getProperty("db.url"));
        dataSource.setUsername(connectionProperties.getProperty("db.username"));
        dataSource.setPassword(connectionProperties.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setTypeAliasesPackage("ua.pb.task.manager.mapper");
        sessionFactoryBean.setMapperLocations(context.getResources("classpath:mapper/*.xml"));
        return sessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    private String getRemoteUrl() {
        return "db:postgresql://" + REMOTE_HOST + ":" + REMOTE_PORT + "/" + "taskmanager";
    }

}
