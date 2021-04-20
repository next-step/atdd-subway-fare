package nextstep.subway.path.domain.enumeration;

import java.util.function.Function;

public enum DiscountType {
    TEENAGER("청소년 요금", 13, 19, originFare -> (originFare - 350) * 20 / 100),
    CHILD("어린이 요금", 6, 13, originFare -> (originFare - 350) * 50 / 100),
    NONE("해당 없음", 0, 0, originFare -> 0);

    private String typeName;
    private int startAge;
    private int endAge;
    private Function<Integer, Integer> expression;

    DiscountType(String typeName, int startAge, int endAge, Function<Integer, Integer> expression) {
        this.typeName = typeName;
        this.startAge = startAge;
        this.endAge = endAge;
        this.expression = expression;
    }

    public int discount(int originFare) {
        return this.expression.apply(originFare);
    }

    public static DiscountType typeFromAge(int age) {
        for (DiscountType type : DiscountType.values()) {
            if (type.startAge <= age && age < type.endAge) {
                return type;
            }
        }

        return DiscountType.NONE;
    }
}
