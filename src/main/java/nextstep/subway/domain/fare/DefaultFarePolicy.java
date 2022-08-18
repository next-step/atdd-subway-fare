package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class DefaultFarePolicy implements FarePolicy {

    private static final int MINIMUM_FARE = 1250;

    @Override
    public int fare(Path path) {
        return MINIMUM_FARE;
    }
}
