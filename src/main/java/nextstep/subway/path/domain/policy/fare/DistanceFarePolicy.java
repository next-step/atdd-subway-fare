package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.exception.NotSupportedDistanceFareRule;

import java.util.List;

public class DistanceFarePolicy implements FarePolicy {
    private final List<DistanceFareRule> distanceFareRules;

    public DistanceFarePolicy(List<DistanceFareRule> distanceFareRules) {
        this.distanceFareRules = distanceFareRules;
    }

    @Override
    public int calculateFare(Path path) {
        int totalDistance = path.getTotalDistance();
        DistanceFareRule distanceFareRule = findStatisfiedDistanceFareRule(totalDistance);

        return distanceFareRule.calculateDistanceFare(totalDistance);
    }

    private DistanceFareRule findStatisfiedDistanceFareRule(int totalDistance) {
        return distanceFareRules.stream()
                .filter(rule -> rule.isSatisfiedBy(totalDistance))
                .findAny()
                .orElseThrow(NotSupportedDistanceFareRule::new);
    }
}
