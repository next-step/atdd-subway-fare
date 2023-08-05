package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscount {

    TEEN(age -> age >= 13 && age < 19, 0.2),
    CHILD(age -> age >= 6 && age < 13, 0.5),
    ADULT(age -> age >= 19, 0.0);

    private final Predicate<Integer> ageRange;
    private final double discountRate;

    AgeDiscount(Predicate<Integer> ageRange, double discountRate) {
        this.ageRange = ageRange;
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static AgeDiscount getByAge(int age) {
        return Arrays.stream(values())
                .filter(ageDiscount -> ageDiscount.ageRange.test(age))
                .findFirst()
                .orElse(AgeDiscount.ADULT);
    }

    public static boolean isDiscount(int age) {
        return getByAge(age).discountRate > 0;
    }
}
