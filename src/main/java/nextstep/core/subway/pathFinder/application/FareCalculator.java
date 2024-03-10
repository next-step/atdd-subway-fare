package nextstep.core.subway.pathFinder.application;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class FareCalculator {

    public static final int BASE_FARE = 1250;

    public static final int STANDARD_DISTANCE = 10;
    public static final int STANDARD_ADDITIONAL_DISTANCE = 5;

    public static final int LONG_DISTANCE = 50;
    public static final int LONG_ADDITIONAL_DISTANCE = 8;

    public static final int ADDITIONAL_FARE_PER_KM = 100;

    // TODO: 정책이라는 의미를 갖는 클래스로 분리할 것
    public int calculateTotalFare(int distance, List<Integer> additionalFares) {
        return calculateDistanceFare(distance) + findMaxAdditionalFare(additionalFares);
    }

    private int calculateDistanceFare(int distance) {
        int fare = BASE_FARE;

        if (distance > STANDARD_DISTANCE && distance <= LONG_DISTANCE) {
            fare += calculateDistanceOverFare(distance - STANDARD_DISTANCE, STANDARD_ADDITIONAL_DISTANCE);
        }
        if (distance > LONG_DISTANCE) {
            fare += calculateDistanceOverFare(LONG_DISTANCE - STANDARD_DISTANCE, STANDARD_ADDITIONAL_DISTANCE);
            fare += calculateDistanceOverFare(distance - LONG_DISTANCE, LONG_ADDITIONAL_DISTANCE);
        }
        return fare;
    }

    private int calculateDistanceOverFare(int distance, int additionalDistance) {
        return (int) (((double) ((distance - 1) / additionalDistance) + 1) * ADDITIONAL_FARE_PER_KM);
    }

    private int findMaxAdditionalFare(List<Integer> additionalFares) {
        return additionalFares.stream()
                .mapToInt(value -> value)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
