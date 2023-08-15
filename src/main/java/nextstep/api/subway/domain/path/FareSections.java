package nextstep.api.subway.domain.path;

import java.util.function.LongSupplier;
import java.util.stream.Stream;

public enum FareSections {
    UNDER_10 {
        private static final long BASE_DISTANCE = 0;
        private static final long DEFAULT_FARE = 1250;

        @Override
        public long calculateFarePerSection(final long distance) {
            return calculateFareOverBase(distance, BASE_DISTANCE, () -> DEFAULT_FARE);
        }
    },
    UNDER_50 {
        private static final long BASE_DISTANCE = 10;
        private static final long LIMIT_DISTANCE = 50;
        private static final long FARE_PER_TERM = 100;
        private static final long TERM = 5;

        @Override
        public long calculateFarePerSection(final long distance) {
            return calculateFareOverBase(distance, BASE_DISTANCE, () -> {
                if (distance <= LIMIT_DISTANCE) {
                    return calculateAdditionalFarePerTerm(distance - BASE_DISTANCE, FARE_PER_TERM, TERM);
                }
                return calculateFarePerSection(LIMIT_DISTANCE);
            });
        }
    },
    OVER_50 {
        private static final long BASE_DISTANCE = 50;
        private static final long FARE_PRE_TERM = 100;
        private static final long TERM = 8;

        @Override
        public long calculateFarePerSection(final long distance) {
            return calculateFareOverBase(distance, BASE_DISTANCE,
                    () -> calculateAdditionalFarePerTerm(distance - BASE_DISTANCE, FARE_PRE_TERM, TERM));
        }
    },
    ;

    public static long calculateTotalFare(final long distance) {
        return Stream.of(values())
                .mapToLong(it -> it.calculateFarePerSection(distance))
                .sum();
    }

    abstract long calculateFarePerSection(final long distance);

    private static long calculateAdditionalFarePerTerm(final long distance, final long fare, final long term) {
        return (int) ((((distance - 1) / term) + 1) * fare);
    }

    private static long calculateFareOverBase(final long distance, final long base, final LongSupplier calculator) {
        if (distance < base + 1) {
            return 0;
        }
        return calculator.getAsLong();
    }
}
