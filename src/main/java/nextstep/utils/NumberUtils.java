package nextstep.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class NumberUtils {

    public static int requirePositiveNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number required positive");
        }

        return number;
    }

}
