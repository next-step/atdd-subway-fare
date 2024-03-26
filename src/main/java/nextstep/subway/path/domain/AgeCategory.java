package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeCategory {
    CHILD(6, 12, 0.5, 350),
    TEENAGER(13, 18, 0.8, 350),
    ADULT(19, 200, 1.0, 0);

    private final int minAge;
    private final int maxAge;
    private final double fareRate;
    private final int fixedDiscount;

    AgeCategory(int minAge, int maxAge, double fareRate, int fixedDiscount) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.fareRate = fareRate;
        this.fixedDiscount = fixedDiscount;
    }

    public static AgeCategory find(Integer memberAge) {
        return Arrays.stream(AgeCategory.values())
                .filter(ageCategory -> ageCategory.minAge <= memberAge && memberAge <= ageCategory.maxAge)
                .findFirst()
                .orElse(null);
    }

    public int discountedFare(int totalFare) {
        return (int) ((totalFare - fixedDiscount) * fareRate);
    }
}
