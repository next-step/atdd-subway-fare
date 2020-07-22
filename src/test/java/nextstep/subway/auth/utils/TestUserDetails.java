package nextstep.subway.auth.utils;

import nextstep.subway.auth.application.UserDetails;

public class TestUserDetails implements UserDetails {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Override
    public Object getPrincipal() {
        return EMAIL;
    }

    @Override
    public Object getCredentials() {
        return PASSWORD;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return true;
    }
}
