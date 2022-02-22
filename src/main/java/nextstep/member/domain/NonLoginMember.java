package nextstep.member.domain;

public class NonLoginMember extends LoginMember {
    private static int NON_LOGIN_AGE = 20;

    public NonLoginMember() {
        super(null, null, null, NON_LOGIN_AGE);
    }
}
