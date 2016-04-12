package ua.pb.task.manager.api;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.pb.task.manager.service.activiti.ProcessInstanceHolder;

import java.io.*;
import java.util.Base64;

/**
 * Created by Mike on 4/9/2016.
 */
@Controller
@RequestMapping("/schema")
public class SchemaController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessInstanceHolder instanceHolder;

    @RequestMapping
    public String showPage() {
        return "common/diagram";
    }

    @RequestMapping("/image")
    @ResponseBody
    public String showDiagram() throws IOException {
        ProcessInstance instance = instanceHolder.getInstance();
        ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        BpmnModel model = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        InputStream inputStream = generator.generatePngDiagram(model);
        byte[] image = IOUtils.toByteArray(inputStream);
        return Base64.getEncoder().encodeToString(image);
    }
}
