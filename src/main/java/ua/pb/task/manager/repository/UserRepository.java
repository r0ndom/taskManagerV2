package ua.pb.task.manager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.pb.task.manager.repository.mapper.UserMapper;
import ua.pb.task.manager.model.User;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Repository
public class UserRepository {

    @Autowired
    private UserMapper mapper;

    public void store(User user) {
        mapper.store(user);
    }
}
