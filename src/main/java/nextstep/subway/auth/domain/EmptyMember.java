package nextstep.subway.auth.domain;


import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.members.member.domain.LoginMember;

public class EmptyMember implements UserDetails {

    public static final String EMPTY = "EMPTY";
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
