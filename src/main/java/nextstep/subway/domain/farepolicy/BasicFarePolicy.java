package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.farepolicy.base.FarePolicy;

public class BasicFarePolicy implements FarePolicy {
    private static final int BASIC_COST = 1250;

    @Override
    public int calculate(Path path) {
        if (dontTake(path.extractDistance())) {
            return 0;
        }
        return BASIC_COST;
    }

    private boolean dontTake(int distance) {
        return distance <= 0;
    }
}
