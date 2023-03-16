package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgeFareType {
    BABY(1, age -> age >= 0 && age < 6),
    CHILD(0.5, age -> age >= 6 && age < 13),
    YOUTH(0.2, age -> age >= 13 && age < 19),
    ADULT(0, age -> age >= 19 && age < 65),
    ELDER(1, age -> age >= 65);

    private static final int DEDUCTION_FARE = 350;
    private static final int NO_DISCOUNT_FARE = 0;

    private final double discountRate;
    private final IntPredicate ageCheck;

    AgeFareType(double discountRate, IntPredicate ageCheck) {
        this.discountRate = discountRate;
        this.ageCheck = ageCheck;
    }

    public static AgeFareType findByAge(int age) {
        return Arrays.stream(values())
                .filter(ageFareType -> ageFareType.match(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이가 음수일 수 없습니다."));
    }

    private boolean match(int distance) {
        return this.ageCheck.test(distance);
    }

    public int discountFare(int totalCharge) {
        if (this == ADULT) {
            return NO_DISCOUNT_FARE;
        }

        if (this == BABY || this == ELDER) {
            return totalCharge;
        }

        return (int) ((totalCharge - DEDUCTION_FARE) * discountRate) + DEDUCTION_FARE;
    }
}
