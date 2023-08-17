package nextstep.subway.path.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class MiddleDistanceFarePolicy extends DefaultDistanceFarePolicy {
    @Override
    public boolean satisfiesCondition(int totalDistance) {
        return SHORT_DISTANCE_LIMIT < totalDistance
                && totalDistance <= MIDDLE_DISTANCE_LIMIT;
    }

    @Override
    public int calculateFare(int totalDistance) {
        int lastDistance = totalDistance - SHORT_DISTANCE_LIMIT;
        return BASIC_FEE + calculateOverFare(lastDistance, MIDDLE_DISTANCE_UNIT);
    }
}
