package ua.pb.task.manager.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Mike on 3/28/2016.
 */
@Controller
//TODO handle params, maybe need to move to json format
public class ErrorController {

    @RequestMapping("/error")
    public String showErrorPage() {
        return "common/error";
    }

}
