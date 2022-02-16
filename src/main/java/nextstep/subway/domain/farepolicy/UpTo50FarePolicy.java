package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.farepolicy.base.DistanceFarePolicy;

public class UpTo50FarePolicy extends DistanceFarePolicy {
    private static final DistanceFareRange DISTANCE_FARE_RANGE = new DistanceFareRange(10, 50);
    private static final int FARE_DISTANCE_UNIT = 5;
    private static final int FARE_PER_UNIT = 100;

    @Override
    protected DistanceFareRange distanceFareRange() {
        return DISTANCE_FARE_RANGE;
    }

    @Override
    protected int fareDistanceUnit() {
        return FARE_DISTANCE_UNIT;
    }

    @Override
    protected int farePerUnit() {
        return FARE_PER_UNIT;
    }
}
