package nextstep.auth.domain;

public class LoginMember implements Account {
    private String email;

    public LoginMember(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
