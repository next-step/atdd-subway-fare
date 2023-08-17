package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;
import nextstep.line.domain.Sections;
import nextstep.member.domain.Member;

public class FareCalculator {

    private final DistanceFarePolicies distanceFarePolicies;
    private final DiscountFarePolicies discountFarePolicies;

    public FareCalculator() {
        this.distanceFarePolicies = new DistanceFarePolicies();
        this.discountFarePolicies = new DiscountFarePolicies();
    }

    public Integer getFare(Sections sections, Member member) {
        Integer distance = sections.getDistance();
        Integer fare = distanceFarePolicies.getFare(distance) + sections.getSurcharge();
        return discountFarePolicies.getDiscountFare(fare, member);
    }

}
