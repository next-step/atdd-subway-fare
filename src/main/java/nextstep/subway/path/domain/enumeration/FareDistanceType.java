package nextstep.subway.path.domain.enumeration;

import java.util.function.Function;

public enum FareDistanceType {
    DEFAULT("기본 요금", 0, 10, distance -> 0),
    OVER_10_KM_BELOW_50_KM("10KM 초과 50KM 이하", 10, 50, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    OVER_50_KM("50KM 초과", 50, Integer.MAX_VALUE, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100));

    private final static int DEFAULT_DISTANCE = 10;
    private String typeName;
    private int rangeStart;
    private int rangeEnd;
    private Function<Integer, Integer> expression;

    FareDistanceType(String typeName, int rangeStart, int rangeEnd, Function<Integer, Integer> expression) {
        this.typeName = typeName;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.expression = expression;
    }

    public int calucate(int distance) {
        return expression.apply(distance - DEFAULT_DISTANCE);
    }

    public static FareDistanceType typeFromDistance(int distance) {

        for (FareDistanceType type : FareDistanceType.values()) {
            if (type.rangeStart < distance && distance <= type.rangeEnd) {
                return type;
            }
        }

        return FareDistanceType.DEFAULT;
    }
}
