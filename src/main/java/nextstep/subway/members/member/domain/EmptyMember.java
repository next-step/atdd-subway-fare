package nextstep.subway.members.member.domain;


public class EmptyMember extends LoginMember {

    public static final String EMPTY = "EMPTY";

    public EmptyMember() {
        super(null, EMPTY, EMPTY, null);
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
