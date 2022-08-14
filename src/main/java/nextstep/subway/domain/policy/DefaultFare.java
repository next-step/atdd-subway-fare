package nextstep.subway.domain.policy;

import org.springframework.stereotype.Component;

@Component
public class DefaultFare implements FarePolicy {

    private static final int DEFAULT_FARE = 1_250;

    @Override
    public boolean supports(int distance) {
        return true;
    }

    @Override
    public int fare(int distance) {
        return DEFAULT_FARE;
    }
}
