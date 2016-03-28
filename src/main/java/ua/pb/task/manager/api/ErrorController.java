package ua.pb.task.manager.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Mike on 3/28/2016.
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/")
    public ModelAndView showErrorPage() {
        return new ModelAndView("common/error");
    }

}
