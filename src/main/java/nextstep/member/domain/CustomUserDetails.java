package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private String email;
    private String password;

    public CustomUserDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
