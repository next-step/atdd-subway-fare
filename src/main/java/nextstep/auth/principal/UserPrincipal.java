package nextstep.auth.principal;

public class UserPrincipal {
    private String email;
    private String role;
    private Integer age;

    public UserPrincipal(String email, String role, Integer age) {
        this.email = email;
        this.role = role;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isTeenager() {
        return age >= 13 && age < 19;
    }

    public boolean isChildren() {
        return age >= 6 && age < 13;
    }

    public boolean isAdult() {
        return age >= 19;
    }
}
