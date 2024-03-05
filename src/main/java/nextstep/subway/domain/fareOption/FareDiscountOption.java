package nextstep.subway.domain.fareOption;

import nextstep.member.domain.Member;

public interface FareDiscountOption {
    boolean isDiscountTarget(Member member);
    int calculateFare(int fare);
}
