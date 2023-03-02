package nextstep.subway.domain;

import java.math.BigDecimal;

public class Discount {
    private static final int YOUTH_AGE = 19;
    private static final int CHILDREN_AGE = 13;
    private static final int DISCOUNT_FARE = 350;
    private static final double YOUTH_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    private final int age;

    public Discount(int age) {
        validationAge(age);

        this.age = age;
    }

    private void validationAge(int age) {
        if (age < 6) {
            throw new IllegalArgumentException("사용자 나이가 6세 미민일 수 없음");
        }
    }

    public BigDecimal discountFare(BigDecimal fare) {
        BigDecimal discountFare = fare;

        if (isWithInChildrenAge()) {
            discountFare = discountFare.subtract(new BigDecimal(DISCOUNT_FARE)).multiply(new BigDecimal(1 - CHILDREN_DISCOUNT_RATE));
            return discountFare;
        }

        if (isWithinYouthAge()) {
            discountFare = discountFare.subtract(new BigDecimal(DISCOUNT_FARE)).multiply(new BigDecimal(1 - YOUTH_DISCOUNT_RATE));
        }

        return discountFare;
    }

    private boolean isWithinYouthAge() {
        return YOUTH_AGE > age;
    }

    private boolean isWithInChildrenAge() {
        return CHILDREN_AGE > age;
    }
}
