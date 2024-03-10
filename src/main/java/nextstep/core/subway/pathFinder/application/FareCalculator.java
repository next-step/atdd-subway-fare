package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.domain.Line;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class FareCalculator {

    public static final int BASE_FARE = 1250;

    public static final int STANDARD_DISTANCE = 10;
    public static final int STANDARD_ADDITIONAL_DISTANCE = 5;

    public static final int LONG_DISTANCE = 50;
    public static final int LONG_ADDITIONAL_DISTANCE = 8;

    public static final int ADDITIONAL_FARE_PER_KM = 100;

    public int calculateFare(int distance, Set<Integer> additionalFares) {
        int fare = BASE_FARE;

        if (distance > STANDARD_DISTANCE && distance <= LONG_DISTANCE) {
            fare += calculateOverFare(distance - STANDARD_DISTANCE, STANDARD_ADDITIONAL_DISTANCE);
        }
        if (distance > LONG_DISTANCE) {
            fare += calculateOverFare(LONG_DISTANCE - STANDARD_DISTANCE, STANDARD_ADDITIONAL_DISTANCE);
            fare += calculateOverFare(distance - LONG_DISTANCE, LONG_ADDITIONAL_DISTANCE);
        }

        return fare + findMaxAdditionalFare(additionalFares);
    }

    private int calculateOverFare(int distance, int additionalDistance) {
        return (int) (((double) ((distance - 1) / additionalDistance) + 1) * ADDITIONAL_FARE_PER_KM);
    }

    private int findMaxAdditionalFare(Set<Integer> additionalFares) {
        return additionalFares.stream()
                .mapToInt(value -> value)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
