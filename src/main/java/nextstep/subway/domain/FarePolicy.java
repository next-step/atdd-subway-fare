package nextstep.subway.domain;

import java.util.Arrays;

public enum FarePolicy {
    DEFAULT(1250, 100, 0, 10, 0),
    OVER_10KM(1250, 100,10, 50, 5),
    OVER_50KM(1250, 100, 50, Integer.MAX_VALUE, 8);

    private final int defaultFare;
    private final int minDistance;
    private final int maxDistance;
    private final int extraFare;
    private final int extraDistance;

    FarePolicy(int defaultFare, int extraFare, int minDistance, int maxDistance, int extraDistance) {
        this.defaultFare = defaultFare;
        this.extraFare = extraFare;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.extraDistance = extraDistance;
    }

    public static FarePolicy createFarePolicy(int distance) {
        return Arrays.stream(FarePolicy.values())
                .filter(fare -> fare.isWithinDistance(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isWithinDistance(int distance) {
        return minDistance < distance && distance <= maxDistance;
    }

    public int getFare(int distance) {
        return calculateFare(defaultFare, distance);
    }

    private int calculateFare(int fare, int distance) {
        if (distance <= 10) {
            return fare;
        }
        if (distance <= 50) {
            return fare + calculateOverFare10(distance - 10);
        }
        return fare + calculateOverFare10(40) + calculateOverFare50(distance - 50);
    }

    private int calculateOverFare10(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFare50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
