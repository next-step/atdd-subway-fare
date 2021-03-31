package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.TEN_DISTANCE;
import static nextstep.subway.path.domain.policy.FarePolicy.calculateOverFare;

public class OverTenDistancePolicy implements FarePolicy {

    private final int distance;

    public OverTenDistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return BASIC_FARE + calculateOverFare(distance - TEN_DISTANCE, PER_FIVE_KILLO);
    }
}
