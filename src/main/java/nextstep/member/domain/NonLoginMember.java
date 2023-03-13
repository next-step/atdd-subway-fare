package nextstep.member.domain;

import java.util.List;

public class NonLoginMember extends LoginMember {
    private static final int nonLoginAge = 25;

    public NonLoginMember() {
        super(null, nonLoginAge, List.of(RoleType.NON_LOGIN_MEMBER.name()));
    }
}
