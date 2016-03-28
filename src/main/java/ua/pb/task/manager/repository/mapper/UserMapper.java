package ua.pb.task.manager.repository.mapper;


import ua.pb.task.manager.model.Role;
import ua.pb.task.manager.model.User;

import java.util.List;

public interface UserMapper {
    void store(User user);
    User findByEmail(String email);
//    User load(String id);
//    void update(User user);
//    void delete(String id);
//    List<User> findAll();
//    List<User> findByRole(Role role);
}
