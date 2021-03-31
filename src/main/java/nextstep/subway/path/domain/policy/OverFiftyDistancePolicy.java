package nextstep.subway.path.domain.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.policy.DistancePolicyFactory.*;
import static nextstep.subway.path.domain.policy.FarePolicy.calculateOverFare;

public class OverFiftyDistancePolicy implements FarePolicy {

    private final int distance;

    public OverFiftyDistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        int over50Distance = distance - FIFTY_DISTANCE;
        int over10Between50Distance = distance - over50Distance - TEN_DISTANCE;
        return BASIC_FARE + calculateOverFare(over10Between50Distance, PER_FIVE_KILLO) + calculateOverFare(over50Distance, PER_EIGHT_KILLO);
    }
}
