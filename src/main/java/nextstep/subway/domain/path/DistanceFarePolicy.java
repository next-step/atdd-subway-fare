package nextstep.subway.domain.path;

import nextstep.member.domain.MemberAge;
import nextstep.subway.domain.fare.BasicFare;
import nextstep.subway.domain.fare.FarePolicy;
import nextstep.subway.domain.fare.FirstFarePolicy;
import nextstep.subway.domain.fare.SecondFarePolicy;
import nextstep.subway.domain.fare.ThirdFarePolicy;


public class DistanceFarePolicy {

    private final FarePolicy farePolicy;

    public DistanceFarePolicy(final int maxAdditionalFare) {
        farePolicy = new BasicFare(maxAdditionalFare);

        FirstFarePolicy firstFarePolicy = new FirstFarePolicy();
        SecondFarePolicy secondFarePolicy = new SecondFarePolicy();
        ThirdFarePolicy thirdFarePolicy = new ThirdFarePolicy();

        farePolicy.setNextPolicyChain(firstFarePolicy);
        firstFarePolicy.setNextPolicyChain(secondFarePolicy);
        secondFarePolicy.setNextPolicyChain(thirdFarePolicy);
    }

    public int calculateFare(final int totalDistance, final MemberAge memberAge) {
        int originalFare = farePolicy.calculateFare(totalDistance);

        return memberAge.discountFare(originalFare);
    }
}
