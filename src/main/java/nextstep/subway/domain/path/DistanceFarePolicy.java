package nextstep.subway.domain.path;

import nextstep.subway.domain.fare.FarePolicy;
import nextstep.subway.domain.fare.FirstFarePolicy;
import nextstep.subway.domain.fare.SecondFarePolicy;
import nextstep.subway.domain.fare.ThirdFarePolicy;

public class DistanceFarePolicy {

    private final FarePolicy farePolicy;

    public DistanceFarePolicy() {
        farePolicy = new FirstFarePolicy();
        SecondFarePolicy secondFarePolicy = new SecondFarePolicy();
        ThirdFarePolicy thirdFarePolicy = new ThirdFarePolicy();

        farePolicy.setNextPolicyChain(secondFarePolicy);
        secondFarePolicy.setNextPolicyChain(thirdFarePolicy);
    }

    public int calculateFare(final int totalDistance) {
        return farePolicy.calculateFare(totalDistance);
    }
}
