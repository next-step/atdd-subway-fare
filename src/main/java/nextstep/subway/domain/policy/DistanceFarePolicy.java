package nextstep.subway.domain.policy;

public class DistanceFarePolicy extends FarePolicy {
    private DistanceType distanceType;
    private int distance;

    public DistanceFarePolicy(int distance) {
        this(distance, null);
    }

    public DistanceFarePolicy(int distance, FarePolicy farePolicy) {
        super(farePolicy);
        this.distance = distance;
        this.distanceType = DistanceType.of(distance);
    }

    @Override
    public int calculate() {
        return distanceType.calculate(distance);
    }
}
