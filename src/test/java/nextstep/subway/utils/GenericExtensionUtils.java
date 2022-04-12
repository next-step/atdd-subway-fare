package nextstep.subway.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericExtensionUtils {

    public static <T> List<T> arrayToList(T[] array) {
        return Arrays.stream(array)
                .collect(Collectors.toList());
    }

    private GenericExtensionUtils() {
    }
}
