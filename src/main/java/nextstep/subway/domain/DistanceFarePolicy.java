package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy {
    private static final int OVER_FARE_PER_DISTANCE = 100;
    private static final int DEFAULT_DISTANCE = 10;
    private int distance;

    private DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    public static DistanceFarePolicy of(int distance) {
        return new DistanceFarePolicy(distance);
    }

    @Override
    public int calculate(int fare) {
        int overDistance = this.distance - DEFAULT_DISTANCE;
        if (overDistance > 0) {
            fare += calculateOverFare11to50(Math.min(overDistance, 40));
            overDistance = Math.max(overDistance - 40, 0);
        }
        if (overDistance > 0) {
            fare += calculateOverFareOver50(overDistance);
        }

        return fare;
    }

    /**
     * 11 ~ 50km 까지 5km 단위로 추가 요금
     *
     * @param distance
     * @return
     */
    private int calculateOverFare11to50(float distance) {
        return (int) (Math.ceil(distance / 5) * OVER_FARE_PER_DISTANCE);
    }

    /**
     * 50km 초과 시 8km 단위로 추가 요금
     *
     * @param distance
     * @return
     */
    private int calculateOverFareOver50(float distance) {
        return (int) (Math.ceil(distance / 8) * OVER_FARE_PER_DISTANCE);
    }
}
