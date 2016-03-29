package ua.pb.task.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.pb.task.manager.service.AuthService;
import ua.pb.task.manager.model.TokenInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void googleAuth(TokenInfo info) throws IOException {
        authService.auth(info);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String signIn() throws IOException {
        return authService.grantAuthorities();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "auth/login";
    }
}
