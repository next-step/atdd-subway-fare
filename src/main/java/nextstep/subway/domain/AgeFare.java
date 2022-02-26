package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum AgeFare {
    CHILD_FARE(6, 12, BigDecimal.valueOf(350), BigDecimal.valueOf(0.5)),
    YOUTH_FARE(13, 18, BigDecimal.valueOf(350), BigDecimal.valueOf(0.2)),
    GENERAL_FARE(19, 200, BigDecimal.ZERO, BigDecimal.ZERO);

    private final int minAge;
    private final int maxAge;
    private final BigDecimal deductibleFare;
    private final BigDecimal discountRate;

    AgeFare(int minAge, int maxAge, BigDecimal deductibleFare, BigDecimal discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductibleFare = deductibleFare;
        this.discountRate = discountRate;
    }

    public static AgeFare findAgeFareType(int age) {
        return Arrays.stream(AgeFare.values())
                .filter(ageFare -> ageFare.isAge(age))
                .findAny()
                .orElse(AgeFare.GENERAL_FARE);
    }

    public BigDecimal extractDiscountFare(BigDecimal fare) {
        if (this == AgeFare.GENERAL_FARE) {
            return fare;
        }

        BigDecimal subtract = fare.subtract(deductibleFare);
        BigDecimal discountFare = subtract.multiply(discountRate);
        return fare.subtract(discountFare);
    }

    private boolean isAge(int age) {
        return minAge <= age && age <= maxAge;
    }
}
