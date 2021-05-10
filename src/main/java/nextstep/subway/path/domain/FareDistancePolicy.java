package nextstep.subway.path.domain;

import nextstep.subway.path.domain.enumeration.FareDistanceType;

public class FareDistancePolicy implements FarePolicy {

    private FareDistanceType fareDistanceType;
    private int distance;

    public FareDistancePolicy(int distance) {
        this(distance, FareDistanceType.typeFromDistance(distance));
    }

    private FareDistancePolicy(int distance, FareDistanceType distanceType) {
        this.distance = distance;
        this.fareDistanceType = distanceType;
    }

    private FareDistancePolicy() {}

    @Override
    public int calculate(int beforeCost) {
        return beforeCost + this.fareDistanceType.calucate(this.distance);
    }
}
