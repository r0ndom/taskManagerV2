package ua.pb.task.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.pb.task.manager.aspect.annotation.AdminAccess;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.repository.UserRepository;

import java.util.List;

/**
 * Created by Mednikov on 29.03.2016.
 */
@Controller
@AdminAccess
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository repository;

    @RequestMapping
    public String showPage() {
        return "users/list";
    }

    @RequestMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<User> getData(String type) {
        return repository.find(type);
    }

}
