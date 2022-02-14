package nextstep.subway.domain.farepolicy;

public final class DistanceFarePolicy implements FarePolicy {
    private final DistanceFareRange distanceFareRange;
    private final int fareDistanceUnit;
    private final int ratePerUnit;

    public DistanceFarePolicy(DistanceFareRange distanceFareRange, int fareDistanceUnit, int ratePerUnit) {
        this.distanceFareRange = distanceFareRange;
        this.fareDistanceUnit = fareDistanceUnit;
        this.ratePerUnit = ratePerUnit;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= distanceFareRange.getMinRange()) {
            return 0;
        }
        int unitSize = distanceForFare(distance) / fareDistanceUnit + 1;
        return unitSize * ratePerUnit;
    }

    private int distanceForFare(int distance) {
        return Math.min(distance - distanceFareRange.getMinRange(), distanceFareRange.getMaxRange());
    }
}
