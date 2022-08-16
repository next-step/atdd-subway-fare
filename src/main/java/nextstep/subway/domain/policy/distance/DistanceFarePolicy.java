package nextstep.subway.domain.policy.distance;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.policy.BasicFarePolicy;

public class DistanceFarePolicy implements BasicFarePolicy {
    @Override
    public int calculate(int age, int fare, int distance, Path path) {
        DistanceType policy = DistanceType.createFarePolicy(distance);
        return policy.calculate(distance);
    }
}
