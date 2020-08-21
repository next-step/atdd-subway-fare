package nextstep.subway.auth.domain;


import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.members.member.domain.LoginMember;

public class EmptyMember implements UserDetails {

    public static final String EMPTY = "EMPTY";

//    public EmptyMember() {
//        this(null, null, null, null);
//    }
//
//    public EmptyMember(Long id, String email, String password, Integer age) {
//        super(id, email, password, age);
//    }

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
}
