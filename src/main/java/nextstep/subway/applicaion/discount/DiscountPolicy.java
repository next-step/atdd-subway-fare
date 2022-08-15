package nextstep.subway.applicaion.discount;

import nextstep.member.domain.Member;

public interface DiscountPolicy {

    boolean isTarget(Member member);

    int discount(int fare);

}
