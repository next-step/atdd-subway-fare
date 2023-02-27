package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    NONE(0, 10, distance -> Fare.from(0)),
    BETWEEN_11_AND_50_EXTRA_FARE(11, 50, distance -> Fare.from(calculateFiveIntervalExtraFare(distance))),
    BETWEEN_50_AND_MAX_EXTRA_FARE(51, Integer.MAX_VALUE, distance -> Fare.from(calculateEightIntervalExtraFare(distance)));

    private static final int FARE_EXTRA = 100;
    private static final int DISTANCE_BASE = 10;
    private static final int DISTANCE_FIVE_INTERVAL_MAX = 50;
    private static final int KM_FIVE = 5;
    private static final int KM_EIGHT = 8;

    private final Integer from;
    private final Integer to;
    private final Function<Integer, Fare> fareFunction;

    DistanceFarePolicy(final Integer from, final Integer to, final Function<Integer, Fare> fareFunction) {
        this.from = from;
        this.to = to;
        this.fareFunction = fareFunction;
    }

    public static Fare from(final Path path) {
        return findFarePolicy(path.getShortestDistance()).fareFunction.apply(path.getShortestDistance());
    }

    private static DistanceFarePolicy findFarePolicy(final Integer distance) {
        return Arrays.stream(values())
                .filter(distanceFarePolicy -> distanceFarePolicy.contain(distance))
                .findFirst()
                .orElse(NONE);
    }

    private static int calculateFiveIntervalExtraFare(final Integer distance) {
        return (int) ((Math.floor((double) (distance - DISTANCE_BASE) / KM_FIVE)) * FARE_EXTRA);
    }

    private static int calculateEightIntervalExtraFare(final Integer distance) {
        double extraDistance = Math.floor((double) (distance - DISTANCE_FIVE_INTERVAL_MAX) / KM_EIGHT);
        return (int) extraDistance * FARE_EXTRA + calculateFiveIntervalExtraFare(DISTANCE_FIVE_INTERVAL_MAX);
    }

    private boolean contain(final Integer distance) {
        return this.from <= distance && this.to >= distance;
    }
}
