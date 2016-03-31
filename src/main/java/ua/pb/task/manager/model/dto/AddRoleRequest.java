package ua.pb.task.manager.model.dto;

import java.util.List;

/**
 * Created by Mednikov on 31.03.2016.
 */
public class AddRoleRequest {
    private Long userId;
    private List<String> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
