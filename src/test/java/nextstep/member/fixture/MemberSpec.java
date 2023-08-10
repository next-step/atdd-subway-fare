package nextstep.member.fixture;

import nextstep.member.domain.Member;

import static nextstep.utils.UnitTestUtils.createEntityTestId;

public class MemberSpec {
    private MemberSpec() {
    }

    public static Member of() {
        final Member member = new Member("yuseongan@next.com", "password", 27, "USER");
        createEntityTestId(member, 1L);

        return member;
    }

    public static Member of(String email, int age) {
        final Member member = new Member(email, "password", age, "USER");
        createEntityTestId(member, 1L);

        return member;
    }
}
