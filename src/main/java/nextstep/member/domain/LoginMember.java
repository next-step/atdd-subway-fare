package nextstep.member.domain;

import nextstep.auth.application.UserDetails;

public class LoginMember {
    private String email;
    private UserDetails userDetails;

    public LoginMember(final String email) {
        this.email = email;
    }

    public LoginMember(final String email, final UserDetails userDetails) {
        this.email = email;
        this.userDetails = userDetails;
    }

    public String getEmail() {
        return email;
    }
}
