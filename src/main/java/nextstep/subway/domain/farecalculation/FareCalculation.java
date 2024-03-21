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

    public static int getFareByAge(int fare, int age) {
        if (AgeType.CHILDREN.isInRange(age)) {
            return (int) Math.round((fare - DISCOUNT) * CHILDREN_DISCOUNT_RATE);
        } else if (AgeType.TEENAGE.isInRange(age)) {
            return (int) Math.round((fare - DISCOUNT) * TEENAGE_DISCOUNT_RATE);
        } else {
            return fare;
        }
    }

    public static int getFareByDistance(Long distance) {
        int baseFare = calculateBaseFare(distance);
        int additionalFare = calculateAdditionalFare(distance);
        return baseFare + additionalFare;
    }

    private static int calculateBaseFare(Long distance) {
        if (distance <= 10) {
            return BASE_FARE;
        } else {
            return BASE_FARE + calculationDistance50Under(Math.min(distance - 10L, 40L));
        }
    }

    private static int calculateAdditionalFare(Long distance) {
        if (distance <= 50) {
            return 0;
        } else {
            return calculationDistance50Over(distance - 50L);
        }
    }

    private static int calculationDistance50Under(Long distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int calculationDistance50Over(Long distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}

