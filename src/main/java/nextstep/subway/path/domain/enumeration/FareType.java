package nextstep.subway.path.domain.enumeration;

import java.util.function.Function;

public enum FareType {
    DEFAULT("기본 요금", distance -> 0),
    OVER_10_KM_BELOW_50_KM("10KM 초과 50KM 이하", distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    OVER_50_KM("50KM 초과", distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100));

    private final static int DEFAULT_FARE = 1_250;
    private final static int DEFAULT_DISTANCE = 10;
    private String typeName;
    private Function<Integer, Integer> expression;

    FareType(String typeName, Function<Integer, Integer> expression) {
        this.typeName = typeName;
        this.expression = expression;
    }

    public int calucate(int distance) {
        return DEFAULT_FARE + expression.apply(distance - DEFAULT_DISTANCE);
    }
}
