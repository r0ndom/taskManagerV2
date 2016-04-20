package ua.pb.task.manager.api;

import org.activiti.engine.form.FormProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.pb.task.manager.aspect.annotation.SessionAccess;
import ua.pb.task.manager.model.*;
import ua.pb.task.manager.model.filter.TaskSearchFilter;
import ua.pb.task.manager.repository.UserRepository;
import ua.pb.task.manager.service.activiti.ActivitiService;
import ua.pb.task.manager.util.FormUtils;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mednikov on 28.03.2016.
 */
@Controller
@SessionAccess
@RequestMapping("/app/tasks")
public class TaskController {

    @Autowired
    private ActivitiService service;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private FormUtils formUtils;
    @Autowired
    private RequestUtil requestUtil;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showSearchPage() {
        return getMav(service.findAll());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView search(TaskSearchFilter filter) {
        return getMav(service.search(filter));
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public ModelAndView sort(String str) {
        return getMav(service.findAll((str != null && !str.isEmpty()) ? Order.NATURAL : Order.DESC));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView showCreatePage() {
        ModelAndView mav = new ModelAndView("process/create");
        String taskId = service.startProcess();
        List<FormProperty> formPropertyList = service.getFormProperty(taskId);
        mav.addObject("taskData", formUtils.convertTo(formPropertyList));
        mav.addObject("taskId", taskId);
        return mav;
    }

    @RequestMapping(value = "/submitTaskForm", method = RequestMethod.POST)
    public String submit(FormData data) {
        System.out.println("Form data id: " + data.getId());
        if (service.getFormKey(data.getId()).equals("expectedTime")&&!data.getMap().containsKey("edit")) {
            data.getMap().put("edit", "false");
        }
        //TODO fix this fucking piece of shit
        service.defineExec(service.getTaskExecutionIdById(data.getId()));
        String executionId = service.submitForm(data);
        String taskId = service.getTaskIdByExecutionId(executionId);
        if (data.getMap().containsKey("status")) {
            if (data.getMap().get("status").equals("завершена")) {
                service.deleteTask(taskId);
                return "redirect:/app/tasks/";
            }
        }
        return "redirect:/app/tasks/show/" + taskId;
    }

    @RequestMapping(value = "/editTask/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") String execId) {
        ModelAndView mav = new ModelAndView("process/details");
        String taskId = service.getTaskIdByExecutionId(execId);
        FormData formData = new FormData(taskId);
        List<FormProperty> currentFormPropertyList = service.getFormProperty(taskId);
        for(FormProperty formProperty: currentFormPropertyList) {
            if(formProperty.isRequired()){
                formData.put(formProperty.getId(), "");
            }
            if(formProperty.getType().getName().equals("enum")){
                formData.put(formProperty.getId(), "оценённая");
            }
        }
        formData.put("edit", "true");
        String executionId = service.submitForm(formData);
        String nextTaskId = service.getTaskIdByExecutionId(executionId);
        List<FormProperty> nextFormPropertyList = service.getFormProperty(nextTaskId);
        mav.addObject("taskData", formUtils.convertTo(nextFormPropertyList));
        mav.addObject("isWritable", true);
        mav.addObject("execId", execId);
        mav.addObject("taskId", nextTaskId);
        mav.addObject("isSubmit", true);
        mav.addObject("hasComment", false);
        return mav;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getTaskData(@PathVariable("id") String taskId) {
        ModelAndView mav = new ModelAndView("process/details");
        List<FormProperty> formPropertyList = service.getFormProperty(taskId);
        Iterator<FormProperty> iterator = formPropertyList.iterator();
        while (iterator.hasNext()) {
            FormProperty formProperty = iterator.next();
            if(Objects.equals(formProperty.getId(), "edit")) {
                iterator.remove();
            }
        }
        String id = service.getTaskExecutionIdById(taskId);
        mav.addObject("taskData", formUtils.convertTo(formPropertyList));
        mav.addObject("comments", service.getComments(id));
        mav.addObject("isWritable", true);
        mav.addObject("taskId", taskId);
        mav.addObject("execId", id);
        mav.addObject("isSubmit", true);
        mav.addObject("hasComment", true);
        return mav;
    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public ModelAndView showTaskData(@PathVariable("id") String taskId, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("process/details");
        UserSession session = requestUtil.getUserSession(request);
        User user = userDao.find(session.getId());
        String execId = service.getTaskExecutionIdById(taskId);
        List<FormProperty> formPropertyList = service.getFormProperty(taskId);
        Iterator<FormProperty> iterator = formPropertyList.iterator();
        while (iterator.hasNext()){
            FormProperty formProperty = iterator.next();
            if(Objects.equals(formProperty.getId(), "edit")) {
                iterator.remove();
                mav.addObject("editDescr", service.checkDeleteAccess(user, taskId));
            }
        }
        mav.addObject("taskData", formPropertyList);
        mav.addObject("isWritable", false);
        mav.addObject("taskId", taskId);
        mav.addObject("execId", execId);
        mav.addObject("isEditor", service.checkUserAccess(user, taskId));
        mav.addObject("isDeleted", service.checkDeleteAccess(user, taskId));
        return mav;
    }

    @RequestMapping(value = "/archive/", method = RequestMethod.GET)
    public ModelAndView showArchive() {
        ModelAndView mav = new ModelAndView("archive/archive");
        mav.addObject("tasks", service.getArchivedTasks());
        return mav;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteTaskData(@PathVariable("id") String taskId) {
        service.deleteTask(taskId);
        return "redirect:/app/tasks/";
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public String addComment(Comment comment, HttpServletRequest request) {
        UserSession session = requestUtil.getUserSession(request);
        User user = userDao.find(session.getId());
        String taskId = service.getTaskIdByExecutionId(comment.getTaskId());
        service.createComment(user, comment.getTaskId(), comment.getText());
        return "redirect:/app/tasks/" + taskId;
    }

    @RequestMapping(value = "/deploy", method = RequestMethod.GET)
    public String deploy() {
        service.deploy();
        return "redirect:/app/tasks/";
    }

    @ModelAttribute("formData")
    public FormData getFormData() {
        return new FormData();
    }

    @ModelAttribute("taskSearchFilter")
    public TaskSearchFilter getFilter() {
        return new TaskSearchFilter();
    }

    @ModelAttribute("comment")
    public Comment getComment() {
        return new Comment();
    }

    private ModelAndView getMav(List<TaskData> list) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("tasks", list);
        mav.addObject("statusList", service.getStatuses());
        mav.addObject("executorList", userDao.findAll());
        mav.addObject("authorList", userDao.findAll());
        return mav;
    }
}
