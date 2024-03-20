package nextstep.subway.domain.farecalculation;

import nextstep.auth.domain.AgeType;

public class FareCalculation {

    private static final int BASE_FARE = 1250;
    private static final int DISCOUNT = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGE_DISCOUNT_RATE = 0.8;

    public static int getFareCalculation(Long distance, int age, Long additionalFare) {
        int fare = getFareByDistance(distance);
        return getFareByAge(fare + additionalFare.intValue(), age);
    }

    public static int getFareByDistance(Long distance) {
        int additionalFare = 0;
        if (distance <= 10) {
            return BASE_FARE;
        } else if (distance <= 50) {
            additionalFare = calculationDistance50Under(distance - 10L);
        } else {
            additionalFare = calculationDistance50Under(40L)
                    + calculationDistance50Over(distance - 50L);
        }
        return BASE_FARE + additionalFare;
    }

    public static int getFareByAge(int fare, int age) {
        if (AgeType.CHILDREN.isInRange(age)) {
            return (int) Math.round((fare - DISCOUNT) * CHILDREN_DISCOUNT_RATE);
        } else if (AgeType.TEENAGE.isInRange(age)) {
            return (int) Math.round((fare - DISCOUNT) * TEENAGE_DISCOUNT_RATE);
        } else {
            return fare;
        }
    }

    private static int calculationDistance50Under(Long distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int calculationDistance50Over(Long distance) {
        return ((int) (Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
