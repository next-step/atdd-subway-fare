package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DefaultLinePolicy implements FarePolicy {

    public static final int BASIC_LINE_FARE = 0;

    @Override
    public int calculateFare(int fare) {
        return fare + BASIC_LINE_FARE;
    }
}
