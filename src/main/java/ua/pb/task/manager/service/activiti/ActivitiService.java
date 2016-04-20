package ua.pb.task.manager.service.activiti;

import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.model.*;
import ua.pb.task.manager.model.filter.TaskSearchFilter;
import ua.pb.task.manager.repository.UserRepository;
import ua.pb.task.manager.repository.mapper.ArchiveMapper;
import ua.pb.task.manager.repository.mapper.CommentMapper;
import ua.pb.task.manager.repository.mapper.FilterMapper;
import ua.pb.task.manager.util.PropertyReader;

import java.util.*;

/**
 * Created by Mednikov on 05.01.2016.
 */

//TODO refactor this shit
@Service
public class ActivitiService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private ArchiveMapper archiveMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FilterMapper mapper;

    public String getFormKey(String taskId){
        return taskService.createTaskQuery().taskId(taskId).singleResult().getFormKey();
    }

    public String submitForm(FormData formData) {
        String id = formData.getId();
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        removeLongStringSymbols(formData.getMap());
        formService.submitTaskFormData(id, formData.getMap());
        return task.getExecutionId();
    }

    //FIXME rename method to some another name
    private void removeLongStringSymbols(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (entry.getValue().length() > 255) {
                entry.setValue(entry.getValue().substring(0, 255));
            }
        }
    }

    public String getTaskIdByExecutionId(String executionId) {
        Task task = taskService.createTaskQuery().executionId(executionId).singleResult();
        return task.getId();
    }

    public String getTaskExecutionIdById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task.getExecutionId();
    }

    public List<FormProperty> getFormProperty(String taskId) {
        return formService.getTaskFormData(taskId).getFormProperties();
    }

    public List<TaskData> findAll() {
        List<Task> query = taskService.createTaskQuery().list();
        deleteUnusedTask(query);
        return generateTaskDataList(query);
    }

    public List<TaskData> findAll(Order order) {
        List<Task> query = taskService.createTaskQuery().list();
        deleteUnusedTask(query);
        List<TaskData> result = generateTaskDataList(query);
        if (!order.equals(Order.NATURAL))
            Collections.sort(result, order.getTaskDataComparator());
        return result;
    }

    private void deleteUnusedTask(List<Task> tasks) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Task task = iterator.next();
            if (getFormKey(task.getId()).equals("createTask") && isTaskHasEmptyFields(task)) {
                runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "");
                iterator.remove();
            }
        }
    }

    public boolean isTaskHasEmptyFields(Task task) {
        List<FormProperty> formProperties = getFormProperty(task.getId());
        for (FormProperty property : formProperties) {
            if (property.getValue() == null || property.getValue().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public List<TaskData> search(TaskSearchFilter filter) {
        List<Task> result = new ArrayList<Task>();
        List<Task> query = taskService.createTaskQuery().list();
        for (Task task : query) {
            Map<String, Object> params = getParamsByExecutionId(task.getExecutionId());
            String status = params.get("status") != null ? getString(params.get("status")) : "new";
            String author = getString(params.get("author"));
            String executor = getString(params.get("executor"));
            if (status.equals(filter.getStatus())||filter.getStatus().equals("")) {
                if (author.equals(filter.getAuthor())||filter.getAuthor().equals(""))
                    if(executor.equals(filter.getExecutor())||filter.getExecutor().equals(""))
                        result.add(task);
            }

        }
        deleteUnusedTask(result);
        return generateTaskDataList(result);
    }

    public boolean checkUserAccess(User currentUser, String id) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        Map<String, Object> params = getParamsByExecutionId(task.getExecutionId());
        String ldap = getString(params.get("executor"));
        if (ldap == null || ldap.equals("")) {
            return !currentUser.getMainEmail().equals(getString(params.get("author")));
        }

        User executor = userDao.findByEmail(ldap);
        return executor != null && currentUser.getMainEmail().equals(ldap);
    }

    public boolean checkDeleteAccess(User currentUser, String id) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        Map<String, Object> params = getParamsByExecutionId(task.getExecutionId());
        String ldap = getString(params.get("author"));
        if (ldap == null || ldap.equals("")) {
            return false;
        }
        return currentUser.getMainEmail().equals(ldap);
    }

    private Map<String, Object> getParamsByExecutionId(String id) {
        Map<String, Object> params = new LinkedHashMap<>();
        Task task = taskService.createTaskQuery().executionId(id).singleResult();
        params.put("state", task.getFormKey());
        params.putAll(taskService.getVariables(task.getId()));
        return params;
    }

    public Map<String, Object> getParamsByTaskId(String taskId){
        Map<String, Object> params = new HashMap<String, Object>();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        params.put("state", task.getFormKey());
        params.putAll(taskService.getVariables(task.getId()));
        return params;
    }


    public String startProcess() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("process");
        String processId = instance.getProcessInstanceId();
        TaskQuery query = taskService.createTaskQuery().processInstanceId(processId);
        Task task = query.singleResult();
        return task.getId();
    }

    public void redeploy() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deployments)
            repositoryService.deleteDeployment(deployment.getId());
        repositoryService.createDeployment()
                .addClasspathResource("/process/process.bpmn20.xml")
                .deploy();
    }

    private List<TaskData> generateTaskDataList(List<Task> tasks) {
        List<TaskData> result = new ArrayList<TaskData>();
        for (Task task : tasks) {
            result.add(generateTaskData(task));
        }
        return result;
    }

    private TaskData generateTaskData(Task task) {
        Map<String, Object> params = getParamsByExecutionId(task.getExecutionId());
        TaskData taskData = new TaskData(convertMap(params));
        taskData.setActivitiDynamicId(task.getExecutionId());
        taskData.setId(task.getId());
        return taskData;
    }

    private String getString(Object object) {
        return object != null ? String.valueOf(object) : "";
    }

    private Map<String, String> convertMap(Map<String, Object> params) {
        Map<String, String> newParams = new HashMap<>();
        for(Map.Entry<String, Object> entry: params.entrySet()) {
            newParams.put(entry.getKey(), getString(entry.getValue()));
        }
        return newParams;
    }


    public Map<String,String> getStatuses() {
        Properties properties = PropertyReader.getProperties("statuses.properties");
        Map<String,String> map = new HashMap<>();
        for (final String name: properties.stringPropertyNames())
            map.put(name, properties.getProperty(name));
        return map;
    }

    public void deleteTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        archiveMapper.put(generateTaskData(task));
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "");
    }

    public void createComment(User user, String taskId, String text) {
        Comment comment = new Comment(null, taskId, user.getMainEmail(), text, new Date());
        commentMapper.create(comment);
    }

    public void updateComment(Comment comment) {
        comment.setDate(new Date());
        commentMapper.update(comment);
    }

    public List<Comment> getComments(String taskId) {
        return commentMapper.findByTaskId(taskId);
    }

    public void deleteComment(Long id) {
        commentMapper.delete(id);
    }

    public List<Archive> getArchivedTasks() {
        return archiveMapper.findAll();
    }


    public void defineExec(String execId) {
        String id = getTaskIdByExecutionId(execId);
        Map<String, Object> map = getParamsByTaskId(id);
        String developer = null;
        for (String key : map.keySet()) {
            if (key.equals("developer"))
                developer = (String) map.get(key);
        }
        if (developer != null)
            runtimeService.setVariable(execId, "executor", developer);
    }

    public void deploy() {
        repositoryService.createDeployment()
                .addClasspathResource("process/process.bpmn20.xml")
                .deploy();
    }
}
