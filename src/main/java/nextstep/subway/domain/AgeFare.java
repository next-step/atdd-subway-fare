package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum AgeFare {
    CHILD_FARE(6, 12, BigDecimal.valueOf(350), BigDecimal.valueOf(50)),
    YOUTH_FARE(13, 18, BigDecimal.valueOf(350), BigDecimal.valueOf(20)),
    GENERAL_FARE(19, 200, BigDecimal.ZERO, BigDecimal.ZERO);

    private int minAge;
    private int maxAge;
    private BigDecimal deductibleFare;
    private BigDecimal discountRate;

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

    private boolean isAge(int age) {
        return minAge <= age && age <= maxAge;
    }
}
