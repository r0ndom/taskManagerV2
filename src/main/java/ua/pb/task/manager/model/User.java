package ua.pb.task.manager.model;

import ua.pb.task.manager.model.dto.UserDto;
import ua.pb.task.manager.util.EmailValidator;
import ua.pb.task.manager.util.KeyGenerator;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class User
{
    public static final String USER_KEY_NAME_COOKIE = "tm";

    private Long id;
    private Set<String> emails;
    private Set<Role> roles;

    private User() {
    }

    public Set<String> getEmails() {
        return emails;
    }

    public Long getId() {
        return id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public static Builder newBuilder() {
        return new User().new Builder();
    }


    //TODO fix this dirty hook
    public static User newInstance(List<UserDto> list) {
        User user = new User();
        Set<String> emails = new HashSet<>();
        Set<Role> roles = new HashSet<>();
        for (UserDto dto : list) {
            user.id = dto.getId();
            emails.add(dto.getEmail());
            roles.add(dto.getRole());
        }
        user.emails = emails;
        user.roles = roles;
        return user;
    }

    public class Builder {

        private Builder() {
            User.this.emails = new HashSet<>();
            User.this.roles = new HashSet<>();
        }

        public Builder addEmail(String email) {
            checkNotNull(email, "Not valid email");
            checkArgument(EmailValidator.validate(email), "Not valid email");
            emails.add(email);
            return this;
        }

        public Builder addRole(Role role) {
            checkNotNull(role, "Not valid role");
            roles.add(role);
            return this;
        }

        public Builder setEmails(Set<String> emails) {
            checkArgument(EmailValidator.validateList(emails), "Not valid email list");
            User.this.emails = emails;
            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            User.this.roles = roles;
            return this;
        }

        public User build() {
            checkNotNull(emails, "Emails list is null");
            checkNotNull(roles, "Roles list is null");
            checkArgument(roles.size() > 0,  "User must contain more then zero roles");
            checkArgument(emails.size() > 0, "User must contain more then zero emails");
            User.this.id = KeyGenerator.getId();
            return User.this;
        }
    }
}
