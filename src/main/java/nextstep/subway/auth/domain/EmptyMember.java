package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.UserDetails;

public class EmptyMember implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static final String EMPTY = "EMPTY";

    public EmptyMember() {
        this.id = null;
        this.email = EMPTY;
        this.password = EMPTY;
        this.age = null;
    }

    @Override
    public Object getPrincipal() {
        return EMPTY;
    }

    @Override
    public Object getCredentials() {
        return EMPTY;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
