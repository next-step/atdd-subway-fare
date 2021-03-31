package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.Fare.DEFAULT_FARE;

public class FareDefaultRuleStrategy implements FareRuleStrategy {

    @Override
    public int calculateFare(int distance) {
        return DEFAULT_FARE;
    }
}
