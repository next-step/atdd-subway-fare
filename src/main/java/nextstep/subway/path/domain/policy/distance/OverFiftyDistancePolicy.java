package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.*;

public class OverFiftyDistancePolicy implements FarePolicy {
    public static final int TEN_DISTANCE = 10;
    private static final int PER_FIVE_KILLO = 5;
    private static final int PER_EIGHT_KILLO = 8;

    private final int distance;

    public OverFiftyDistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare(int fare) {
        int over50Distance = distance - FIFTY_DISTANCE;
        int over10Between50Distance = distance - over50Distance - TEN_DISTANCE;
        return fare + calculateOverFare(over10Between50Distance, PER_FIVE_KILLO) + calculateOverFare(over50Distance, PER_EIGHT_KILLO);
    }

    private int calculateOverFare(int distance, int perKillo) {
        return (int) ((Math.ceil((distance - 1) / perKillo) + 1) * 100);
    }
}
