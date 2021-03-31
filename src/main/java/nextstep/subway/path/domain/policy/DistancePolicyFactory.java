package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DistancePolicyFactory {
    private static final int TEN_DISTANCE = 10;
    private static final int FIFTY_DISTANCE = 50;

    public static FarePolicy findPolicy(int distance) {
        if (distance > FIFTY_DISTANCE) {
            return new OverFiftyDistancePolicy(distance);
        }
        if (distance > TEN_DISTANCE) {
            return new OverTenDistancePolicy(distance);
        }
        return new DefaultDistancePolicy();
    }
}
