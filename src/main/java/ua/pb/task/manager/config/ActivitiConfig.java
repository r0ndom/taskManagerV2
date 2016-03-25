package ua.pb.task.manager.config;

import ua.pb.task.manager.form.TextAreaFormType;
import ua.pb.task.manager.form.UsersFormType;
import org.activiti.engine.*;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

/**
* Created by Mednikov on 26.02.2016.
*/
@Configuration
@PropertySource("classpath:activiti.properties")
public class ActivitiConfig {

    @Value("${activiti.dbType}")
    private String dbType;
    @Value("${activiti.dbSchemaUpdate}")
    private String dbSchemaUpdate;
    @Value("${activiti.jobExecutorActivate}")
    private Boolean jobExecutorActivate;
    @Value("${activiti.deploymentResources}")
    private Resource deploymentResources;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseType(dbType);
        processEngineConfiguration.setDatabaseSchemaUpdate(dbSchemaUpdate);
        processEngineConfiguration.setJobExecutorActivate(jobExecutorActivate);
        processEngineConfiguration.setDeploymentResources(new Resource[]{deploymentResources});
        processEngineConfiguration.setCustomFormTypes(Arrays.asList(textAreaFormType(), usersFormFormType()));
        return processEngineConfiguration;
    }

    @Bean
    public AbstractFormType textAreaFormType() {
        return new TextAreaFormType();
    }

    @Bean
    public AbstractFormType usersFormFormType() {
        return new UsersFormType();
    }

    @Bean
    public ProcessEngine processEngine() throws Exception {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return processEngineFactoryBean.getObject();
    }

    @Bean
    public FormService formService() throws Exception {
        return processEngine().getFormService();
    }

    @Bean
    public TaskService taskService() throws Exception {
        return processEngine().getTaskService();
    }

    @Bean
    public RepositoryService repositoryService() throws Exception {
        return processEngine().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() throws Exception {
        return processEngine().getRuntimeService();
    }

    @Bean
    public HistoryService historyService() throws Exception {
        return processEngine().getHistoryService();
    }

    @Bean
    public ManagementService managementService() throws Exception {
        return processEngine().getManagementService();
    }
}
