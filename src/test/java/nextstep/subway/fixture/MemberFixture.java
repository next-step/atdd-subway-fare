package nextstep.subway.fixture;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.List;

public class MemberFixture {
    public static Member 어린이 = new Member("children@email.com", "password", 7, List.of(RoleType.ROLE_MEMBER.name()));
    public static Member 청소년 = new Member("teenager@email.com", "password", 7, List.of(RoleType.ROLE_MEMBER.name()));
    public static Member 관리자 = new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name()));
    public static Member 멤버 = new Member("member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));
}
