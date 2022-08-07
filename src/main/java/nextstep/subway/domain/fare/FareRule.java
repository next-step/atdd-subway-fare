package nextstep.subway.domain.fare;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

import static nextstep.utils.NumberUtils.requirePositiveNumber;

@RequiredArgsConstructor
public enum FareRule {

    STANDARD(distance -> distance <= Distances.STANDARD, 0),
    OVER_10KM(distance -> distance <= Distances.FIFTY, 5),
    OVER_50KM(distance -> distance > Distances.FIFTY, 8),
    ;

    private static final int BASIC_FARE = 1250;

    private final Predicate<Integer> predicate;
    private final int everyDistance;


    public static FareRule of(int distance) {
        requirePositiveNumber(distance);

        return Arrays.stream(values())
                .filter(fareRule -> fareRule.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Fare not found"));
    }

    public int getFare(int distance) {
        if (this == STANDARD) {
            return BASIC_FARE;
        }

        return BASIC_FARE + calculateOverFare(distance - Distances.STANDARD);
    }


    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / this.everyDistance) + 1) * 100);
    }

    private static class Distances {
        private static final int STANDARD = 10;
        private static final int FIFTY = 50;
    }
}
