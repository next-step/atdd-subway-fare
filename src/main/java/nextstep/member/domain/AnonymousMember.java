package nextstep.member.domain;

import java.util.List;

public class AnonymousMember extends LoginMember {

    public AnonymousMember() {
        super(-1L, 0, List.of(RoleType.ROLE_ANONYMOUS.name()));
    }
}
