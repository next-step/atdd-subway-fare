package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Member;

import java.util.List;

public class DiscountFarePolicies {

    private final List<DiscountFarePolicy> values;

    public DiscountFarePolicies() {
        values = List.of(new AnonymousDiscountPolicy(),
                new AdultDiscountPolicy(),
                new ChildrenDiscountPolicy(),
                new TeenagerDiscountPolicy());
    }

    public Integer getDiscountFare(int fare, Member member) {
        return values.stream()
                .filter(discountFarePolicy -> discountFarePolicy.isSupport(member))
                .map(discountFarePolicy -> discountFarePolicy.getFare(fare))
                .findAny()
                .orElse(fare);
    }

}
