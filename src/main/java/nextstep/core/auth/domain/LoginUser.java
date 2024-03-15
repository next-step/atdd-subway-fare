package nextstep.core.auth.domain;

public class LoginUser implements UserDetail {
    private String email;

    public LoginUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
