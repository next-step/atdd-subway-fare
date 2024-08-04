package nextstep.subway.domain.entity;

public class DistanceFarePolicy implements FarePolicy {


    public static final long DEFAULT_DISTANCE = 10L;
    public static final int OVER_10KM_DISTANCE = 10;
    public static final int OVER_50KM_DISTANCE = 50;
    public static final int OVER_10KM_FARE_DISTANCE = 5;
    public static final int OVER_50KM_FARE_DISTANCE = 8;
    public static final int OVER_10KM_FARE_AMOUNT = 100;
    public static final int OVER_50KM_FARE_AMOUNT = 100;
    private final long distance;

    public DistanceFarePolicy(long distance) {
        this.distance = distance;
    }

    @Override
    public int getAdditionalFare() {
        if (distance > DEFAULT_DISTANCE) {
            return calculateOverFare(distance);
        }
        return 0;
    }

    private int calculateOverFare(long distance) {
        if (distance > OVER_50KM_DISTANCE) {
            return (int) ((Math.ceil((distance - DEFAULT_DISTANCE - 1) / OVER_50KM_FARE_DISTANCE) + 1)
                * OVER_50KM_FARE_AMOUNT);
        }
        if (distance > OVER_10KM_DISTANCE) {
            return (int) ((Math.ceil((distance - DEFAULT_DISTANCE - 1) / OVER_10KM_FARE_DISTANCE) + 1)
                * OVER_10KM_FARE_AMOUNT);
        }
        return 0;
    }

}
