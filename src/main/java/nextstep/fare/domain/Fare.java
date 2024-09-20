package nextstep.fare.domain;

import java.util.Objects;

public class Fare {

    private static final Long BASE_FARE = 1250L;
    private static final Long ADDITIONAL_FARE = 100L;

    private static final int MINIMUM_DISTANCE = 1;
    private static final int BASE_FARE_DISTANCE_LIMIT = 10;
    private static final int EXTENDED_FARE_DISTANCE_LIMIT = 50;
    private static final int ADDITIONAL_FARE_INTERVAL = 5;
    private static final int REDUCED_ADDITIONAL_FARE_INTERVAL = 8;

    private final Long fare;

    public Fare(Long fare) {
        this.fare = fare;
    }

    public static Fare from(final int distance) {
        validateDistance(distance);
        return new Fare(calculateFare(distance));
    }

    private static void validateDistance(final int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new LessThanMinimumDistanceException(MINIMUM_DISTANCE, distance);
        }
    }

    private static Long calculateFare(final int distance) {
        if (distance <= BASE_FARE_DISTANCE_LIMIT) {
            return BASE_FARE;
        }
        if (distance <= EXTENDED_FARE_DISTANCE_LIMIT) {
            return calculateAdditionalFare(distance);
        }
        return calculateReducedAdditionalFare(distance);
    }

    private static Long calculateAdditionalFare(final int distance) {
        int additionalDistance = Math.min(distance, EXTENDED_FARE_DISTANCE_LIMIT) - BASE_FARE_DISTANCE_LIMIT - 1;
        return BASE_FARE + ADDITIONAL_FARE
                + (additionalDistance / ADDITIONAL_FARE_INTERVAL) * ADDITIONAL_FARE;
    }

    private static Long calculateReducedAdditionalFare(final int distance) {
        int additionalDistance = distance - EXTENDED_FARE_DISTANCE_LIMIT - 1;
        return calculateAdditionalFare(distance)
                + ADDITIONAL_FARE + (additionalDistance / REDUCED_ADDITIONAL_FARE_INTERVAL) * ADDITIONAL_FARE;
    }

    public Long getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
