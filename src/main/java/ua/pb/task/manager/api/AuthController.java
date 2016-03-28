package ua.pb.task.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.pb.task.manager.service.AuthService;
import ua.pb.task.manager.util.TokenHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Controller
public class AuthController {

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String googleAuth(HttpServletRequest request) {
        String error = request.getParameter("error");
        String code = request.getParameter("code");
        tokenHandler.setData(code, error);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void signIn() throws IOException {
        authService.auth();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void signUp() throws IOException {
        authService.register();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }
}
