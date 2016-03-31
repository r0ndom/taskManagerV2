package ua.pb.task.manager.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO thinking about to fill enum from property file
public enum Role  {
    ROLE_DEVELOPER("Разработчик"),
    ROLE_MANAGER("Менеджер"),
    ROLE_ADMIN("Администратор"),
    ROLE_TESTER("Тестировщик"),
    ROLE_GUEST("Гость"),
    ROLE_CONTROL("Администратор приложения");

    private final String roleViewName;

    Role(final String s) {
        roleViewName = s;
    }

    public String getRoleViewName() {
        return roleViewName;
    }

    @Override
    public String toString() {
        return this.roleViewName;
    }


    public static Set<Role> getPermittedValues() {
        Role[] roles = new Role[] {ROLE_ADMIN, ROLE_DEVELOPER, ROLE_MANAGER, ROLE_TESTER, ROLE_GUEST};
        return new HashSet<Role>(Arrays.asList(roles));
    }

    public static Role getRole(String value) {
        Set<Role> roles = getPermittedValues();
        for (Role role : roles) {
            if (role.getRoleViewName().equals(value)) {
                return role;
            }
        }
        return null;
    }

    public static Set<Role> getRoles(List<String> roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(getRole(role));
        }
        return result;
    }

    public static Set<Role> getAcceptedRoles(Set<Role> roles) {
        Set<Role> temp = getPermittedValues();
        temp.removeAll(roles);
        return temp;
    }
}