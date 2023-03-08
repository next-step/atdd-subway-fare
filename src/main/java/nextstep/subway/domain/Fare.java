package nextstep.subway.domain;

import nextstep.member.domain.Member;

public class Fare {
    private Member member;
    private Path path;

    public Fare(Path path) {
        this(null, path);
    }

    public Fare(Member member, Path path) {
        this.member = member;
        this.path = path;
    }

    public int calculate(MemberFarePolicy farePolicy) {
        return farePolicy.calculate(member, path);
    }
}
