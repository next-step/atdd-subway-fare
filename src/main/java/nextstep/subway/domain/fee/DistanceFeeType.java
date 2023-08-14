package nextstep.subway.domain.fee;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFeeType {
    LESS_THAN_10(0, 10, BasicDistanceFee::new),
    BETWEEN_10_AND_50(11, 50, MiddleDistanceFee::new),
    GREATER_THAN_50(51, Integer.MAX_VALUE, LongDistanceFee::new);

    private final int startDistance;
    private final int endDistance;
    private final Function<Integer, DistanceFee> function;

    DistanceFeeType(int startDistance, int endDistance, Function<Integer, DistanceFee> function) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.function = function;
    }

    public static DistanceFee getDistanceFee(int distance) {
        return Arrays.stream(values()).filter(v -> distance >= v.startDistance && distance <= v.endDistance)
                .findAny()
                .map(v -> v.function.apply(distance))
                .orElse(new BasicDistanceFee(distance));
    }
}
