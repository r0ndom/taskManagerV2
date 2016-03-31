package ua.pb.task.manager.repository.mapper;


import org.apache.ibatis.annotations.Param;
import ua.pb.task.manager.model.Role;
import ua.pb.task.manager.model.User;
import ua.pb.task.manager.model.dto.AddRoleRequest;
import ua.pb.task.manager.model.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserMapper {
    void storeUser(User user);
    void storeEmails(User user);
    void storeRoles(User user);
    List<UserDto> findByEmail(String email);
    List<UserDto> findByRole(Role role);
    List<UserDto> findById(Long userId);
    void deleteEmail(Long userId);
    void deleteRole(Long userId);
    void deleteUser(Long id);;
    List<UserDto> findAll();
    void addRoles(@Param("id")Long id, @Param("roles")Set<Role> roles);
}
