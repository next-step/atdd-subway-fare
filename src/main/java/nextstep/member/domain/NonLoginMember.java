package nextstep.member.domain;

import java.util.List;

public class NonLoginMember extends LoginMember {
    private static final Long nonLoginId = -1L;
    private static final int nonLoginAge = 25;

    public NonLoginMember() {
        super(nonLoginId, nonLoginAge, List.of(RoleType.ROLE_MEMBER.name()));
    }
}
