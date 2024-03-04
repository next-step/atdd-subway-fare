package nextstep.subway.domain.path.fee;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceRange {

    UP_TO_10("10이하", distance -> distance.isLessThan(11)) {
        @Override
        public Fare calculate(Distance distance) {
            return new Fare(DEFAULT_ABOVE_10_FARE);
        }
    },
    ABOVE_10_UP_TO_50("10 초과 50 이하", distance -> distance.isGreaterThan(10) && distance.isLessThan(51)) {
        @Override
        public Fare calculate(Distance distance) {
            final int amount = (int) ((Math.ceil((distance.value() - 10 - 1) / 5) + 1) * 100);

            return new Fare(DEFAULT_ABOVE_10_FARE).add(new Fare(amount));
        }
    },
    ABOVE_50("50초과", distance -> distance.isGreaterThan(50)) {
        @Override
        public Fare calculate(Distance distance) {
            final int amount = (int) ((Math.ceil((distance.value() - 50 - 1) / 8) + 1) * 100);

            return new Fare(DEFAULT_ABOVE_50_FARE).add(new Fare(amount));
        }
    };

    private final String description;
    public final Predicate<Distance> rangePredicate;

    private static final int DEFAULT_ABOVE_10_FARE = 1250;
    private static final int DEFAULT_ABOVE_50_FARE = 2050;

    DistanceRange(String description, Predicate<Distance> rangePredicate) {
        this.description = description;
        this.rangePredicate = rangePredicate;
    }

    public static DistanceRange fromDistance(Distance distance) {
        return Arrays.stream(DistanceRange.values())
                .filter(distance::isCurrentRange)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public abstract Fare calculate(Distance distance);
}
