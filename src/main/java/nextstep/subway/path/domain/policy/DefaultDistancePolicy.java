package nextstep.subway.path.domain.policy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;

public class DefaultDistancePolicy implements FarePolicy {

    @Override
    public int calculate() {
        return BASIC_FARE;
    }
}
