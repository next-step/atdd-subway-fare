package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;

public class DefaultDistancePolicy implements FarePolicy {

    @Override
    public int calculate() {
        return BASIC_FARE;
    }
}
