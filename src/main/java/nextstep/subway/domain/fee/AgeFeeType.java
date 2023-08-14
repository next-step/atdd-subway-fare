package nextstep.subway.domain.fee;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeFeeType {
    TODDLER(0, 5, fee -> 0),
    CHILDREN(6, 13, fee -> (int) ((fee - 350) * 0.5)),
    TEENAGER(13, 19, fee -> (int) ((fee - 350) * 0.8)),
    ADULT(19, Integer.MAX_VALUE, fee -> fee);

    private final int startAge;
    private final int endAge;
    private final Function<Integer, Integer> expression;

    AgeFeeType(int startAge, int endAge, Function<Integer, Integer> expression) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.expression = expression;
    }

    public int getDiscountFee(int fee) {
        return expression.apply(fee);
    }

    public static AgeFeeType getAgeFeeTypeByAge(int age) {
        return Arrays.stream(values()).filter(ageFeeType -> ageFeeType.startAge <= age && ageFeeType.endAge > age)
                .findAny().orElse(ADULT);
    }
}
