package nextstep.subway.path.domain;

import nextstep.subway.member.domain.AgeRange;

import java.util.Arrays;

public enum AgeRangeFare {
    BABY(AgeRange.BABY, 1.0, 0),
    CHILDREN(AgeRange.CHILDREN, 0.5, 350),
    TEENAGER(AgeRange.TEENAGER, 0.8, 350),
    ADULT(AgeRange.ADULT, 1.0, 0),
    UNKNOWN(AgeRange.UNKNOWN, 1.0, 0);

    private final AgeRange ageRange;
    private final double discountRate;
    private final int deduction;

    AgeRangeFare(AgeRange ageRange, double discountRate, int deduction) {
        this.ageRange = ageRange;
        this.discountRate = discountRate;
        this.deduction = deduction;
    }

    public static AgeRangeFare from(AgeRange ageRange) {
        return Arrays.stream(AgeRangeFare.values())
                .filter(ageRangeFare -> ageRangeFare.ageRange == ageRange)
                .findFirst()
                .orElse(UNKNOWN);
    }

    public long calculateFare(long fare) {
        if (fare <= deduction) {
            return fare;
        }
        return Math.round((fare - deduction) * discountRate);
    }
}
