package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Path;

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
    public int calculate(Path path) {
        return unitSize(path.extractDistance()) * ratePerUnit;
    }

    private int unitSize(int distance) {
        if (distanceFareRange.isSmallerThan(distance)) {
            return 0;
        }
        int unitSize = distanceForFare(distance) / fareDistanceUnit;
        if (distance > distanceFareRange.getMinRange()) {
            unitSize += 1;
        }
        return unitSize;
    }

    private int distanceForFare(int distance) {
        return Math.min(distance - distanceFareRange.getMinRange(), distanceFareRange.getMaximumChargedRange(fareDistanceUnit));
    }
}
