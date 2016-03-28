package ua.pb.task.manager.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.pb.task.manager.aspect.annotation.SessionAccess;

/**
 * Created by Mednikov on 28.03.2016.
 */
@Controller
@SessionAccess
public class TaskController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("tasks/tasks");
    }
}
