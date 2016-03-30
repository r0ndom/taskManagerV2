package ua.pb.task.manager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.pb.task.manager.model.Role;
import ua.pb.task.manager.model.dto.UserDto;
import ua.pb.task.manager.repository.mapper.UserMapper;
import ua.pb.task.manager.model.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserRepository {

    public static final String REQUESTS = "requests";
    public static final String ALL = "all";

    @Autowired
    private UserMapper mapper;

    public void store(User user) {
        mapper.storeUser(user);
        mapper.storeEmails(user);
        mapper.storeRoles(user);
    }

    public User findByEmail(String email) {
        return User.newInstance(mapper.findByEmail(email));
    }

    public List<User> findByRole(Role role) {
        return User.newList(mapper.findByRole(role));
    }

    public User findMainAdmin() {
        return findByRole(Role.ROLE_CONTROL).get(0);
    }

    //TODO clean session and unauthorize after this action
    public void deleteUser(User user) {
        mapper.deleteRole(user.getId());
        mapper.deleteEmail(user.getId());
        mapper.deleteUser(user.getId());
    }


    //TODO create userservice and move to it
    public List<User> find(String type) {
        switch(type) {
            case REQUESTS:
                return findByRole(Role.ROLE_GUEST);
            case ALL:
                return findAll();
            default:
                return Collections.emptyList();
        }
    }


    public List<User> findAll() {
        return User.newList(mapper.findAll());
    }

}
