package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum DistanceFareType {
    BASIC(0, 0, distance -> distance > 0 && distance <= 10),
    OVER_10KM(5, 10, distance -> distance > 10 && distance <= 50),
    OVER_50KM(8, 50, distance -> distance > 50);

    private static final int BASIC_FARE = 1250;
    private static final int OVER_10KM_MAX_FARE = 2050;
    private static final int EXTRA_UNIT_FARE = 100;

    private final int extraUnitDistance;
    private final int extraBoundaryDistance;
    private final IntPredicate fareTypeCheck;

    DistanceFareType(int extraUnitDistance, int extraBoundaryDistance, IntPredicate fareTypeCheck) {
        this.extraUnitDistance = extraUnitDistance;
        this.extraBoundaryDistance = extraBoundaryDistance;
        this.fareTypeCheck = fareTypeCheck;
    }

    public static DistanceFareType findByDistance(int distance) {
        return Arrays.stream(values())
                .filter(distanceFareType -> distanceFareType.match(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리가 0 이거나 음수일 경우 요금 계산할 수 없습니다."));
    }

    private boolean match(int distance) {
        return this.fareTypeCheck.test(distance);
    }

    public int calculateOverFare(int distance) {
        if (this == BASIC) {
            return BASIC_FARE;
        }

        if (this == OVER_10KM) {
            return BASIC_FARE + calculateExtraFare(distance);
        }

        return OVER_10KM_MAX_FARE + calculateExtraFare(distance);
    }

    private int calculateExtraFare(int distance) {
        return (int) ((Math.ceil((distance - 1 - extraBoundaryDistance) / extraUnitDistance) + 1) * EXTRA_UNIT_FARE);
    }

    public static int calculateFare(int distance) {
        return findByDistance(distance).calculateOverFare(distance);
    }
}
