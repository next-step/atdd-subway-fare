package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountType {
    CHILD(age -> age >= 6 && age < 13, 0.5),
    YOUTH(age -> age >= 13 && age < 20, 0.2),
    ADULT(age -> age >= 20, 0);

    private static final int DEDUCTION = 350;

    private final Predicate<Integer> predicate;
    private final double discountRate;

    DiscountType(Predicate<Integer> predicate, double discountRate) {
        this.predicate = predicate;
        this.discountRate = discountRate;
    }

    public static DiscountType from(int age) {
        return Arrays.stream(values())
                .filter(discountType -> discountType.predicate.test(age))
                .findFirst()
                .orElse(ADULT);
    }

    public int discountFare(int distanceFare) {
        if (this == ADULT) {
            return (int) discountRate;
        }

        return (int) ((distanceFare - DEDUCTION) * discountRate);
    }


}
