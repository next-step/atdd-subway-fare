package nextstep.subway.path.domain.policy.fare;

import org.springframework.stereotype.Component;

@Component
public class LongDistanceFareRule extends DistanceFareRule {
    @Override
    boolean isSatisfiedBy(int totalDistance) {
        return MEDIUM_DISTANCE_LIMIT < totalDistance;
    }

    @Override
    int calculateDistanceFare(int totalDistance) {
        int fareOfMiddleDistance = calculateOverFare(getMiddleDistance(totalDistance), MEDIUM_DISTANCE_UNIT);
        int fareOfLastDistance = calculateOverFare(getLastDistance(totalDistance), LONG_DISTANCE_UNIT);

        return BASIC_FEE + fareOfMiddleDistance + fareOfLastDistance;
    }
}
