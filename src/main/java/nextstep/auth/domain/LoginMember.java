package nextstep.auth.domain;

public class LoginMember {
    private String email;

    public LoginMember() {
    }

    public LoginMember(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return email != null;
    }

    public String getEmail() {
        return email;
    }
}
