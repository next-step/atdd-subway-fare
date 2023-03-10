package nextstep.subway.domain.fare;

public class DistancePolicy implements FarePolicy {
    private static final DistancePolicy INSTANCE = new DistancePolicy();

    private DistancePolicy() {
    }

    public static DistancePolicy getInstance() {
        return INSTANCE;
    }

    @Override
    public Fare addFare(Fare fare, FareBasis fareBasis) {
        int distance = fareBasis.getDistance();
        int distanceFare = DistanceFareType.calculateFare(distance);
        return fare.addDistanceFare(distanceFare);
    }
}
