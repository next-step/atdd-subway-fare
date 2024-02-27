package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeRange {
    BABY(0, 6),
    CHILDREN(6, 13),
    TEENAGER(13, 19),
    ADULT(19, Integer.MAX_VALUE),
    UNKNOWN(Integer.MAX_VALUE, Integer.MAX_VALUE); // 찾을 수 없는 경우를 위한 상수 추가

    private final int minAge;
    private final int maxAge;

    AgeRange(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeRange findBy(Integer age) {
        if (age == null) {
            return UNKNOWN;
        }

        return Arrays.stream(AgeRange.values())
                .filter(ageRange -> age >= ageRange.minAge && age < ageRange.maxAge)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
