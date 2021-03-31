package nextstep.subway.path.domain.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.policy.DistancePolicyFactory.TEN_DISTANCE;
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
