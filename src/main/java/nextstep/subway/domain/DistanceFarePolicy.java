package nextstep.subway.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
    // 정책명이 BASIC, CLOSE, FAR 3개라지만 확장성이 약한 것 같음
    // 그렇다고 OVER10, OVER50이면 수치가 변경됐을 때 반영하기 어려움
    // 정책을 내부적으로 STEP1, STEP2 이런 식으로 정하기로 함
    // BASIC을 분리할까도 생각했으나 too much 같아서 그냥 둠
    BASIC(1250, 0, 10, 0), STEP1(1250, 11, 50, 5), STEP2(2050, 51, Integer.MAX_VALUE, 8);

    final int baseFare;
    final int distanceUnderBound;
    final int distanceUpperBound;
    final int distanceFareBoundary;

    DistanceFarePolicy(
        int baseFare,
        int distanceUnderBound,
        int distanceUpperBound,
        int distanceFareBoundary
    ) {
        this.baseFare = baseFare;
        this.distanceUnderBound = distanceUnderBound;
        this.distanceUpperBound = distanceUpperBound;
        this.distanceFareBoundary = distanceFareBoundary;
    }

    public static int calculateFare(int distance) {
        DistanceFarePolicy distanceFarePolicy = Arrays.stream(values())
            .filter(policy -> policy.isInRange(distance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        if (distanceFarePolicy == BASIC) {
            return distanceFarePolicy.baseFare;
        }

        int distanceCondition = distance - (distanceFarePolicy.distanceUnderBound - 1);
        return distanceFarePolicy.baseFare +
            ((int) Math.ceil(
                (double) distanceCondition / distanceFarePolicy.distanceFareBoundary) * 100
            );
    }

    private boolean isInRange(int distance) {
        return distanceUnderBound <= distance && distance <= distanceUpperBound;
    }
}
