package nextstep.subway.fare.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.LongUnaryOperator;

public enum FarePolicy {
    BASE_FARE(distance -> 1250),
    DISTANCE_BASED(distance -> {
        int startDistance = 10;
        int endDistance = 50;
        if (distance <= 10) {
            return 0;
        }
        long extraDistance = Math.min(distance, endDistance) - startDistance;
        return (int) ((Math.ceil((extraDistance - 1) / 5) + 1) * 100);
    }),
    LONG_DISTANCE(distance -> {
        int longDistanceStartKm = 50;
        if (distance <= longDistanceStartKm) {
            return 0;
        }
        long extraDistance = distance - longDistanceStartKm;
        return (int) ((Math.ceil((extraDistance - 1) / 8) + 1) * 100);
    });

    private final LongUnaryOperator fareCalculator;

    FarePolicy(LongUnaryOperator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public long calculateFare(long distance) {
        return fareCalculator.applyAsLong(distance);
    }

    public static long calculateTotalFare(long distance) {
        return BASE_FARE.calculateFare(distance) +
            DISTANCE_BASED.calculateFare(distance) +
            LONG_DISTANCE.calculateFare(distance);
    }

    public static long calculateTotalFare(long distance, List<Long> additionalFares) {
        long baseFare = BASE_FARE.calculateFare(distance) +
            DISTANCE_BASED.calculateFare(distance) +
            LONG_DISTANCE.calculateFare(distance);

        long maxAdditionalFare = getMaxAdditionalFare(additionalFares);

        return baseFare + maxAdditionalFare;
    }

    private static long getMaxAdditionalFare(List<Long> additionalFares) {
        if (additionalFares == null || additionalFares.isEmpty()) {
            return 0L;
        }
        return additionalFares.stream()
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
    }
}
