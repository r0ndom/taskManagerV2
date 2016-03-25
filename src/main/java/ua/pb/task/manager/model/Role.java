package ua.pb.task.manager.model;

//TODO thinking about to fill enum from property file
public enum Role  {
    ROLE_DEVELOPER("Разработчик"),
    ROLE_MANAGER("Менеджер"),
    ROLE_ADMIN("Администратор"),
    ROLE_TESTER("Тестировщик"),
    ROLE_CONTROL("Control");

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


    public static Role[] getPermittedValues() {
        return new Role[] {ROLE_ADMIN, ROLE_DEVELOPER, ROLE_MANAGER, ROLE_TESTER};
    }
}