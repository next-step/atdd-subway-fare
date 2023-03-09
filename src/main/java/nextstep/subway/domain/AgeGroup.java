package nextstep.subway.domain;

import java.util.Arrays;

public enum AgeGroup {
    CHILDREN(6, 13, 0.5), YOUTH(13, 19, 0.8);

    private final int moreAge;
    private final int underAge;
    private final double discountRate;

    AgeGroup(final int moreAge, final int underAge, final double discountRate) {
        this.moreAge = moreAge;
        this.underAge = underAge;
        this.discountRate = discountRate;
    }

    public static int calculateFare(final int age, final int baseFare) {
        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> ageGroup.isInAgeRange(age))
                .findFirst()
                .map(ageGroup -> (int) (baseFare * ageGroup.discountRate))
                .orElse(baseFare);
    }

    private boolean isInAgeRange(final int age) {
        return this.moreAge <= age && this.underAge > age;
    }
}
