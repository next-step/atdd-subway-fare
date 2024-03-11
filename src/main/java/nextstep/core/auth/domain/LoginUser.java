package nextstep.core.auth.domain;

public class LoginUser extends UserDetail {
    private String email;

    public LoginUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
