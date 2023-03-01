package nextstep.subway.domain;

import java.math.BigDecimal;

public class Fare {
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int MIN_EXCEED_OVER_DISTANCE = 5;
    private static final int MAX_EXCEED_OVER_DISTANCE = 8;
    private static final int MIN_TOTAL_FARE = 1250;
    private static final int OVER_FARE = 100;

    private final int distance;
    private BigDecimal fare;

    public Fare(int distance) {
        this.distance = distance;

        calTotalFare();
    }

    public BigDecimal getFare() {
        return fare;
    }

    private void calTotalFare() {
        if (isWithInMinDistance()) {
            fare = new BigDecimal(MIN_TOTAL_FARE);
        }

        BigDecimal overFare = calculateOverFare(distance - MIN_DISTANCE);
        fare = new BigDecimal(MIN_TOTAL_FARE).add(overFare);
    }

    private boolean isWithInMinDistance() {
        return MIN_DISTANCE >= distance;
    }

    private boolean isWithInMaxDistance() {
        return MAX_DISTANCE >= distance;
    }

    private BigDecimal calculateOverFare(int distance) {
        int overCount = getOverCountByStandardDistance(distance);

        return new BigDecimal(OVER_FARE).multiply(BigDecimal.valueOf(overCount));
    }

    private int getOverCountByStandardDistance(int distance) {
        if (isWithInMaxDistance()) {
            return (int) (Math.ceil((distance - 1) / MIN_EXCEED_OVER_DISTANCE) + 1);
        }

        return (int) (Math.ceil((distance - 1) / MAX_EXCEED_OVER_DISTANCE) + 1);
    }
}
