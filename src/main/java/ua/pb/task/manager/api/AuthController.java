package ua.pb.task.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.pb.task.manager.util.TokenHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Controller
public class AuthController {

    @Autowired
    private TokenHandler tokenHandler;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String googleAuth(HttpServletRequest request) {
        String error = request.getParameter("error");
        String code = request.getParameter("code");
        tokenHandler.setData(code, error);
        return "redirect:/";
    }
}
