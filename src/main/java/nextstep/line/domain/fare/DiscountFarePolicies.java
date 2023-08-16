package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;

import java.util.List;

public class DiscountFarePolicies {

    private final List<DiscountFarePolicy> values;

    public DiscountFarePolicies() {
        values = List.of(new AnonymousDiscountPolicy(),
                new AdultDiscountPolicy(),
                new ChildrenDiscountPolicy(),
                new TeenagerDiscountPolicy());
    }

    public Integer getDiscountFare(int fare, UserPrincipal userPrincipal) {
        return values.stream()
                .filter(discountFarePolicy -> discountFarePolicy.isSupport(userPrincipal))
                .map(discountFarePolicy -> discountFarePolicy.getFare(fare))
                .findAny()
                .orElse(fare);
    }

}
