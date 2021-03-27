package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceOfFareType {

    TEN_KM_OVER_FARE(distance -> distance > 10 && distance <= 50, distance -> (int) (Math.ceil((distance - 11) / 5) + 1) * 100),
    FIFTY_KM_OVER_FARE(distance -> distance > 50, distance -> (int) (((Math.ceil((distance - 51) / 8) + 1) * 100) + 800)),
    BASIC_FARE(distance -> distance > 0 && distance <= 10, distance -> 0);

    private Function<Integer, Boolean> distanceType;
    private Function<Integer, Integer> expression;

    DistanceOfFareType(Function<Integer, Boolean> distanceType, Function<Integer, Integer> expression) {
        this.distanceType = distanceType;
        this.expression = expression;
    }

    public static DistanceOfFareType valueOf(int distance) {
        return Arrays.stream(DistanceOfFareType.values())
                .filter(it -> it.isDistanceType(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리는 0보다 커야합니다."));
    }

    private boolean isDistanceType(int distance) {
        return distanceType.apply(distance);
    }

    public int calculate(int distance) {
        return expression.apply(distance);
    }
}
