package nextstep.subway.domain;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    // open 범위(이상 / 이하)
    CHILD(6, 12, 50), TEEN(13, 18, 20), ADULT(19, Integer.MAX_VALUE, 0);

    private static final int BASE_DISCOUNT_AMOUNT = 350;

    private final int underBoundAge;
    private final int upperBoundAge;
    private final int discountRate;

    AgeDiscountPolicy(int underBoundAge, int upperBoundAge, int discountRate) {
        this.underBoundAge = underBoundAge;
        this.upperBoundAge = upperBoundAge;
        this.discountRate = discountRate;
    }

    public static int discountFromAge(int age, int price) {
        AgeDiscountPolicy ageDiscountPolicy = Arrays.stream(values())
            .filter(discountPolicy -> discountPolicy.isIn(age))
            .findFirst()
            .orElseThrow();

        if (ageDiscountPolicy == ADULT) return price;

        return (int) Math.round((price - BASE_DISCOUNT_AMOUNT) * ((double) (100 - ageDiscountPolicy.discountRate) / 100));
    }

    private boolean isIn(int age) {
        return this.underBoundAge <= age && age <= this.upperBoundAge;
    }
}
