package ua.pb.task.manager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.pb.task.manager.model.dto.UserDto;
import ua.pb.task.manager.repository.mapper.UserMapper;
import ua.pb.task.manager.model.User;

import java.util.List;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserRepository {

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

}
