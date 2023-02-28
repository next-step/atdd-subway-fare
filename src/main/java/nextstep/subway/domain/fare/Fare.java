package nextstep.subway.domain.fare;

import java.util.stream.Stream;

public class Fare {
    public static final int DEFAULT_FARE = 1_250;

    private final int cost;

    public Fare(int distance) {
        this.cost = DEFAULT_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        return Stream.of(FarePolicy.values())
                .filter(policy -> policy.supported(distance))
                .mapToInt(policy -> policy.additionalFare(distance))
                .sum();
    }

    public int cost() {
        return cost;
    }
}
