package nextstep.member.domain;

import java.util.Arrays;

public enum AgeRange {
    BABY(0, 6),
    CHILDREN(6, 13),
    TEENAGER(13, 19),
    ADULT(19, Integer.MAX_VALUE),
    UNKNOWN(Integer.MAX_VALUE, Integer.MAX_VALUE);


    private final int min;
    private final int max;

    AgeRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static AgeRange findByAge(Integer age) {
        if (age == null) {
            return UNKNOWN;
        }

        return Arrays.stream(AgeRange.values())
                .filter(ageRange -> age >= ageRange.min && age < ageRange.max)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
