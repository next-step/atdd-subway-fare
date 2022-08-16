package nextstep.subway.domain.policy.fare;

import org.springframework.stereotype.Component;

@Component
public class DefaultFare implements FarePolicy {

    private static final int DEFAULT_FARE = 1_250;

    @Override
    public boolean supports(PathByFare pathByFare) {
        return true;
    }

    @Override
    public int fare(PathByFare pathByFare) {
        return DEFAULT_FARE;
    }
}
