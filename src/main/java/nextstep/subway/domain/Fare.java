package nextstep.subway.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fare {
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int MIN_EXCEED_OVER_DISTANCE = 5;
    private static final int MAX_EXCEED_OVER_DISTANCE = 8;
    private static final int MIN_TOTAL_FARE = 1250;
    private static final int OVER_FARE = 100;

    private static final int YOUTH_AGE = 19;
    private static final int CHILDREN_AGE = 13;
    private static final int DISCOUNT_FARE = 350;
    private static final double YOUTH_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    private final int distance;
    private final int overFareLine;
    private int age;
    private BigDecimal fare;

    @Builder
    public Fare(boolean loginStatus, int distance, int overFareLine, int age) {
        validation(distance, overFareLine);
        this.distance = distance;
        this.overFareLine = overFareLine;

        calTotalFare();

        if (loginStatus) {
            validationAge(age);
            this.age = age;

            discountTotalFare();
        }
    }

    private void validation(int distance, int overFareLine) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 0이하일 수 없음");
        }

        if (overFareLine < 0) {
            throw new IllegalArgumentException("노선 초과운임이 0이하일 수 없음");
        }
    }

    private void validationAge(int age) {
        if (age < 6) {
            throw new IllegalArgumentException("사용자 나이가 6세 미민일 수 없음");
        }
    }

    public BigDecimal getFare() {
        return fare.setScale(0, RoundingMode.FLOOR);
    }

    private void calTotalFare() {
        if (isWithInMinDistance()) {
            fare = new BigDecimal(MIN_TOTAL_FARE);
            fare = fare.add(new BigDecimal(overFareLine));

            return;
        }

        BigDecimal overFare = calculateOverFare(distance - MIN_DISTANCE);
        fare = new BigDecimal(MIN_TOTAL_FARE).add(overFare).add(new BigDecimal(overFareLine));
    }

    private void discountTotalFare() {
        if (isWithInChildrenAge()) {
            fare = fare.subtract(new BigDecimal(DISCOUNT_FARE)).multiply(new BigDecimal(1 - CHILDREN_DISCOUNT_RATE));
            return;
        }

        if (isWithinYouthAge()) {
            fare = fare.subtract(new BigDecimal(DISCOUNT_FARE)).multiply(new BigDecimal(1 - YOUTH_DISCOUNT_RATE));
            return;
        }
    }

    private boolean isWithinYouthAge() {
        return YOUTH_AGE > age;
    }

    private boolean isWithInChildrenAge() {
        return CHILDREN_AGE > age;
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
