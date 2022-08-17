package nextstep.subway.domain.discount;

import nextstep.member.domain.Member;

public interface DiscountPolicy {
    long discountFare(Member member, long fare);
}