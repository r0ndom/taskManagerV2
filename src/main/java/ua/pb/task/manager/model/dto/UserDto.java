package ua.pb.task.manager.model.dto;

import ua.pb.task.manager.model.Role;

/**
 * Created by Mednikov on 28.03.2016.
 */
public class UserDto {
    private Long id;
    private String email;
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
