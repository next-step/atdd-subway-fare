package nextstep.auth.principal;

public class UserPrincipal {
    private String email;
    private String role;

    public UserPrincipal(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
