package nextstep.subway.domain;

import java.util.Arrays;

public enum DistanceFeeType {
    LESS_THAN_10(0, 10, new BasicDistanceFee()),
    BETWEEN_10_AND_50(11, 50, new MiddleDistanceFee()),
    GREATER_THAN_50(51, Integer.MAX_VALUE, new LongDistanceFee());

    private final int startDistance;
    private final int endDistance;
    private final DistanceFee distanceFee;

    DistanceFeeType(int startDistance, int endDistance, DistanceFee distanceFee) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.distanceFee = distanceFee;
    }

    public static DistanceFee getDistanceFee(int distance) {
        return Arrays.stream(values()).filter(v -> distance >= v.startDistance && distance <= v.endDistance)
                .findAny()
                .map(v -> v.distanceFee)
                .orElse(new BasicDistanceFee());
    }
}
