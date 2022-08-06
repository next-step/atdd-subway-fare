package nextstep.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtils {

    public static int requirePositiveNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number required positive");
        }

        return number;
    }

}
