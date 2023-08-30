package nextstep.subway.path.domain.policy.fare;

import org.springframework.stereotype.Component;

@Component
public class MiddleDistanceFareRule extends DistanceFareRule {
    @Override
    boolean isSatisfiedBy(int totalDistance) {
        return SHORT_DISTANCE_LIMIT < totalDistance && totalDistance <= MEDIUM_DISTANCE_LIMIT;
    }

    @Override
    int calculateDistanceFare(int totalDistance) {
        return BASIC_FEE + calculateOverFare(getMiddleDistance(totalDistance), MEDIUM_DISTANCE_UNIT);
    }
}
