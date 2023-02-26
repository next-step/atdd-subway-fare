package nextstep.subway.domain;

import nextstep.member.domain.Member;

public interface MemberFarePolicy extends FarePolicy {
    int calculate(Member member, Path path);
}
