package ua.pb.task.manager.service.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Mike on 4/9/2016.
 */
@Component
public class ProcessInstanceHolder {

    public static final String PROCESS_DEFINITION_KEY = "process";

    @Autowired
    private RuntimeService runtimeService;

    private ProcessInstance instance;

    public ProcessInstance getInstance() {
        if (instance == null) {
            this.instance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
        }
        return instance;
    }
}
