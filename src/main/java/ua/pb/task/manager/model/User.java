package ua.pb.task.manager.model;

import ua.pb.task.manager.util.EmailValidator;
import ua.pb.task.manager.util.KeyGenerator;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class User
{
    public static final String USER_KEY_NAME_COOKIE = "tm";

    private Long id;
    private List<String> emails;
    private Set<Role> roles;

    private User() {
    }

    public List<String> getEmails() {
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

    public class Builder {

        private Builder() {
            User.this.emails = new ArrayList<>();
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

        public Builder setEmails(List<String> emails) {
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
