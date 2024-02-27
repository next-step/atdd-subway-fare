package nextstep.subway.auth.domain;

public class LoginMember {
    private String email;

    public LoginMember(String email) {
        this.email = email;
    }

    public LoginMember() {

    }

    public String getEmail() {
        return email;
    }
}
