package ua.pb.task.manager.model;

/**
 * Created by Mednikov on 29.03.2016.
 */
public class UserSession {
    private Long id;
    private boolean isAdmin;

    public Long getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private UserSession() {}

    private UserSession(Long id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public static UserSession build(User user) {
        return new UserSession(user.getId(), user.hasRole(Role.ROLE_CONTROL));
    }
}
