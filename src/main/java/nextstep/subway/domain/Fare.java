package nextstep.subway.domain;

import java.util.Arrays;

public enum Fare {
    STANDARD(null, 1250, 1, 10, 10),
    EXTRA_LESS_THAN_50KM(STANDARD, 100, 11, 50, 5),
    EXTRA_OVER_50KM(EXTRA_LESS_THAN_50KM, 100, 51, Integer.MAX_VALUE, 8);

    private Fare parentFare;
    private int amount;
    private int minDistance;
    private int maxDistance;
    private int dividend;

    Fare(Fare parentFare, int amount, int minDistance, int maxDistance, int dividend) {
        this.parentFare = parentFare;
        this.amount = amount;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.dividend = dividend;
    }

    public static int calculateAmount(int distance) {
        Fare fare = valueOfDistance(distance);
        return fare.calculate(distance);
    }

    private static Fare valueOfDistance(int distance) {
        return Arrays.stream(Fare.values())
                .filter(fare -> fare.minDistance <= distance && distance <= fare.maxDistance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private int calculate(int distance) {
        if (distance <= 0) {
            return 0;
        }
        if (parentFare == null) {
            return amount;
        }
        return parentFare.calculate(parentFare.maxDistance)
                + ((int) (Math.ceil(distance - (minDistance - 1) - 1) / dividend) + 1) * amount;
    }
}
