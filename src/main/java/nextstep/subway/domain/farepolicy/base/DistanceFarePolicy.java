package nextstep.subway.domain.farepolicy.base;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.farepolicy.DistanceFareRange;

public abstract class DistanceFarePolicy implements FarePolicy {
    protected DistanceFarePolicy() {}

    @Override
    public int calculate(Path path) {
        return unitSize(path.extractDistance()) * farePerUnit();
    }

    private int unitSize(int distance) {
        DistanceFareRange distanceFareRange = distanceFareRange();
        if (distanceFareRange.isSmallerThan(distance)) {
            return 0;
        }
        int unitSize = distanceForFare(distanceFareRange, distance) / fareDistanceUnit();
        if (distance > distanceFareRange.getMinRange()) {
            unitSize += 1;
        }
        return unitSize;
    }

    private int distanceForFare(DistanceFareRange distanceFareRange, int distance) {
        return Math.min(distance - distanceFareRange.getMinRange(), distanceFareRange.getMaximumChargedRange(fareDistanceUnit()));
    }

    protected abstract DistanceFareRange distanceFareRange();
    protected abstract int fareDistanceUnit();
    protected abstract int farePerUnit();
}
