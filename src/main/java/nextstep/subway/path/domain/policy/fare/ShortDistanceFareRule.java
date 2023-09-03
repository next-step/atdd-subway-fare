package nextstep.subway.path.domain.policy.fare;

import org.springframework.stereotype.Component;

@Component
public class ShortDistanceFareRule extends DistanceFareRule {
    @Override
    boolean isSatisfiedBy(int totalDistance) {
        return totalDistance <= SHORT_DISTANCE_LIMIT;
    }

    @Override
    int calculateDistanceFare(int totalDistance) {
        return BASIC_FEE;
    }
}
