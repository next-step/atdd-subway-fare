package nextstep.subway.path.domain.chainofresponsibility.distance;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.chainofresponsibility.FarePolicy;

public abstract class DistanceFarePolicy implements FarePolicy {
    protected static final int BASIC_FEE = 1250;
    protected static final int SHORT_DISTANCE_LIMIT = 10;
    protected static final int MEDIUM_DISTANCE_LIMIT = 50;
    protected static final int MEDIUM_DISTANCE_UNIT = 5;
    protected static final int LONG_DISTANCE_UNIT = 8;
    private static final int OVER_FARE = 100;

    private FarePolicy farePolicy;

    @Override
    public int calculateFare(Path path) {
        // 거리에 따른 요금 정책을 적용하고
        int fare = calculateDistanceFare(path.getTotalDistance());

        // 노선의 추가 요금 정책에게 나머지 계산을 위임한다.
        int additionalFare = farePolicy.calculateFare(path);  // 이건 더 추상화를 할 수 없을까..?

        return fare + additionalFare;
    }

    // 거리에 따라 다른 요금제를 적용하기 위한 메서드
    protected abstract boolean isSatisfiedBy(int totalDistance);

    protected abstract int calculateDistanceFare(int totalDistance);

    protected int calculateOverFare(int distance, int distanceUnit) {
        return ((distance - 1) / distanceUnit + 1) * OVER_FARE;
    }
}
