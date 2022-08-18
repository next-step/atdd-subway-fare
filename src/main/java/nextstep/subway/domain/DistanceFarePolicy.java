package nextstep.subway.domain;

public abstract class DistanceFarePolicy {

    private final int distance;

    public static final int DEFAULT_DISTANCE = 10;
    public static final int EXCEED_DISTANCE = 50;
    public static final int DEFAULT_FARE = 1250;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    public static DistanceFarePolicy create(int distance) {

        if (distance <= DEFAULT_DISTANCE) {
            return new DefaultFarePolicy(distance);
        }

        if (distance >= EXCEED_DISTANCE) {
            return new ExceedDistanceFarePolicy(distance);
        }

        return new IncludeDistanceFarePolicy(distance);
    }

    public abstract int calculateFare();

    int calculateOverFare(int distance, OverFareType type) {
        return (int) ((Math.ceil((distance - (type.getApplyDistance() + 1)) / type.getPerDistance()) + 1) * type.getAddFare());
    }

    public int getDistance() {
        return distance;
    }

}
