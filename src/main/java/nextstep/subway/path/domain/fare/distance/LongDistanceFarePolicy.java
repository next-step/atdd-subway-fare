package nextstep.subway.path.domain.fare.distance;

import org.springframework.stereotype.Component;

@Component
public class LongDistanceFarePolicy extends DefaultDistanceFarePolicy {
    @Override
    public boolean satisfiesCondition(int totalDistance) {
        return MIDDLE_DISTANCE_LIMIT < totalDistance;
    }

    @Override
    public int calculateFare(int totalDistance) {
        int lastDistance = totalDistance - MIDDLE_DISTANCE_LIMIT;
        int middleDistance = totalDistance - lastDistance - SHORT_DISTANCE_LIMIT;

        return BASIC_FEE + calculateOverFare(middleDistance, MIDDLE_DISTANCE_UNIT) + calculateOverFare(lastDistance, LONG_DISTANCE_UNIT);
    }
}
