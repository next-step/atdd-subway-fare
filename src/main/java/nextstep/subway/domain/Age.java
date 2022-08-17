package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.ToIntFunction;

public enum Age {
    BABY(5, fare -> 0),
    CHILD(12, fare -> (int) (fare - ((fare - 350) * 0.5))),
    TEEN(19, fare -> (int) (fare - ((fare - 350) * 0.2))),
    ADULT(Integer.MAX_VALUE, fare -> fare);

    private final int limit;
    private final ToIntFunction<Integer> discount;

    Age(int limit, ToIntFunction<Integer> discount) {
        this.limit = limit;
        this.discount = discount;
    }

    public static Age getAge(int age) {
        return Arrays.stream(values())
                .filter(currentAge -> currentAge.limit >= age)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int applyDiscounts(int fare) {
        return discount.applyAsInt(fare);
    }

}
