package nextstep.subway.domain.policy;

import org.springframework.stereotype.Component;

@Component
public class OverFiftyExtraFare implements FarePolicy {

    private static final int MINIMUM_DISTANCE = 50;
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_UNIT = 8;

    @Override
    public boolean supports(PathByFare pathByFare) {
        return pathByFare.distance() > MINIMUM_DISTANCE;
    }

    @Override
    public int fare(PathByFare pathByFare) {
        return (int) (Math.ceil((double) (pathByFare.distance() - MINIMUM_DISTANCE) / EXTRA_UNIT) * EXTRA_FARE);
    }
}
