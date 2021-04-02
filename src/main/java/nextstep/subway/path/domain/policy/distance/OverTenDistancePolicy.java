package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.TEN_DISTANCE;

public class OverTenDistancePolicy implements FarePolicy {
    private static final int PER_FIVE_KILLO = 5;

    private final int distance;

    public OverTenDistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare(int fare) {
        return fare + calculateOverFare(distance - TEN_DISTANCE, PER_FIVE_KILLO);
    }

    private int calculateOverFare(int distance, int perKillo) {
        return (int) ((Math.ceil((distance - 1) / perKillo) + 1) * 100);
    }
}
