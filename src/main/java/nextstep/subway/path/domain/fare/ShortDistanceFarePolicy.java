package nextstep.subway.path.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class ShortDistanceFarePolicy extends DefaultDistanceFarePolicy {
    @Override
    public boolean condition(int totalDistance) {
        return totalDistance <= SHORT_DISTANCE_LIMIT;
    }

    @Override
    public int calculateFare(int totalDistance) {
        return BASIC_FEE;
    }
}
