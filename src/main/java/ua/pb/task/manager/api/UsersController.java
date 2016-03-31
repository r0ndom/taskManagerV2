package ua.pb.task.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.pb.task.manager.aspect.annotation.AdminAccess;
import ua.pb.task.manager.aspect.annotation.SessionAccess;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.model.dto.AddRoleRequest;
import ua.pb.task.manager.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Mednikov on 29.03.2016.
 */
@Controller
@SessionAccess
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

    @RequestMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<String> getRoles(Long userId) {
        return repository.getAcceptedRoles(userId);
    }

    @RequestMapping(value = "/add/role", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addRole(@RequestBody AddRoleRequest request) {
        repository.addRoles(request);
    }

}
