package nextstep.auth.domain;

import lombok.Getter;

@Getter
public class LoginMember {
    private String email;
    private boolean anonymous;

    public LoginMember(String email) {
        this.email = email;

        this.anonymous = false;
    }

    private LoginMember() {
        this.anonymous = true;
    }

    public static LoginMember createAnonymous() {
        return new LoginMember();
    }
}
