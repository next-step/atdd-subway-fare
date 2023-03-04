package nextstep.subway.domain;

import static nextstep.subway.domain.DistanceByFare.*;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final FarePolicy farePolicy;

    private final int totalDistance;

    private Fare(FarePolicy farePolicy, int totalDistance) {
        this.farePolicy = farePolicy;
        this.totalDistance = totalDistance;
    }

    public static Fare of(FarePolicy farePolicy, int totalDistance) {
        return new Fare(farePolicy, totalDistance);
    }

    public int get() {
        if (farePolicy != null) {
            return farePolicy.calculateFare(calculate(totalDistance));
        }

        return calculate(totalDistance);
    }

    private int calculate(int distance) {
        if (distance <= DEFAULT_DISTANCE.getValue()) {
            return DEFAULT_FARE;
        }

        if (distance <= DistanceByFare.OVER_BETWEEN_TEN_AND_FIFTY.getValue()) {
            return DEFAULT_FARE + calculateOverFare(
                    distance - DEFAULT_DISTANCE.getValue(),
                    STANDARD_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY.getValue()
            );
        }

        return DEFAULT_FARE + calculateOverFare(
                distance - DEFAULT_DISTANCE.getValue(),
                STANDARD_FARE_DISTANCE_OVER_FIFTY.getValue()
        );
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil(((distance - 1) / (double) kilometer)) + 1) * 100);
    }
}
