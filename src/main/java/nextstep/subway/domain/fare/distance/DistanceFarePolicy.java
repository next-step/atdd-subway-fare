package nextstep.subway.domain;

import lombok.Getter;

@Getter
public abstract class DistanceFarePolicy {
    private final int distance;

    public static final int EXTRA_FARE_INCLUDE_DISTANCE = 10;
    public static final int EXTRA_FARE_EXCEED_DISTANCE = 50;

    public static final int DEFAULT_FARE = 1250;
    public static final int INCLUDE_EXTRA_FARE = 100;
    public static final int EXCEED_EXTRA_FARE = 100;

    public static final int INCLUDE_PER_DISTANCE = 5;
    public static final int EXCEED_PER_DISTANCE = 8;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    public static DistanceFarePolicy create(int distance) {

        if (distance <= EXTRA_FARE_INCLUDE_DISTANCE) {
            return new Below(distance);
        }

        if (distance >= EXTRA_FARE_EXCEED_DISTANCE) {
            return new Exceed(distance);
        }

        return new Include(distance);
    }

    public abstract int calculateFare();

}
