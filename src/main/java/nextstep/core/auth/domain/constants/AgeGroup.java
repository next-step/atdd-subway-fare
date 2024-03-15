package nextstep.core.auth.domain.constants;

import java.util.Arrays;

public enum AgeGroup {
    UNKNOWN(0, 0),
    CHILDREN(1, 12),
    TEENAGE(13, 18),
    ADULT(19, Integer.MAX_VALUE);

    private final int minAge;
    private final int maxAge;

    AgeGroup(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeGroup findAgeGroup(int age) {
        return Arrays.stream(values())
                .filter(ageGroup -> ageGroup.isInAgeGroup(age))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public boolean isInAgeGroup(int age) {
        return age >= minAge && age <= maxAge;
    }
}

