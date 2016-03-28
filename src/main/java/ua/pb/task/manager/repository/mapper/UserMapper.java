package ua.pb.task.manager.repository.mapper;


import ua.pb.task.manager.model.Role;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.model.dto.UserDto;

import java.util.List;

public interface UserMapper {
    void storeUser(User user);
    void storeEmails(User user);
    void storeRoles(User user);
    List<UserDto> findByEmail(String email);
//    User load(String id);
//    void update(User user);
//    void delete(String id);
//    List<User> findAll();
//    List<User> findByRole(Role role);
}
