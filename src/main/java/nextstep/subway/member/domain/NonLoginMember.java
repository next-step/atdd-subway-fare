package nextstep.subway.member.domain;

import nextstep.subway.auth.application.UserDetails;

public class NonLoginMember extends LoginMember {

    public NonLoginMember(){
        super(0L, "", "", 0);
    }
    public NonLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public Object getPrincipal() {
        return getEmail();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return true;
    }
}
