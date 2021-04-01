package nextstep.subway.path.domain.policy.line;

import static nextstep.subway.path.domain.Fare.DEFAULT_FARE;

public class LineFareDefaultPolicy implements LineFarePolicy {

    @Override
    public int calculateFare(int distance) {
        return DEFAULT_FARE;
    }
}
