package nextstep.subway.path.domain.discount.age;

import nextstep.subway.path.exception.NotSupportedAgeTypeException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeType {
    ADULT(age -> 19 <= age),
    TEENAGER(age -> 13 <= age && age < 19),
    CHILDREN(age -> 6 <= age && age < 13);

    private final Predicate<Integer> ageStandard;

    public static AgeType of(Integer age) {
        return Arrays.stream(values())
                .filter(ageType -> ageType.ageStandard.test(age))
                .findAny()
                .orElseThrow(NotSupportedAgeTypeException::new);
    }

    AgeType(Predicate<Integer> ageStandard) {
        this.ageStandard = ageStandard;
    }
}
