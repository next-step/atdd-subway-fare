package nextstep.subway.path;

import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.Path;

public abstract class BasicFarePolicy implements FarePolicy {
    @Override
    public int calculateFare(Path path) {
        int fare = calculateDistanceFare(path);
        int additionalFare = calculateAdditionalFare(path);

        return fare + additionalFare;
    }

    protected abstract int calculateDistanceFare(Path path);

    protected abstract int calculateAdditionalFare(Path path);
}
