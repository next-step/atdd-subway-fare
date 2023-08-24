package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.path.exception.NotSupportedDistanceFarePolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceFarePolicies {
    private final List<DistanceFarePolicy> distanceFarePolicies;

    public DistanceFarePolicies(List<DistanceFarePolicy> distanceFarePolicies) {
        this.distanceFarePolicies = distanceFarePolicies;
    }

    public int calculateFare(int totalDistance) {
        DistanceFarePolicy distanceFarePolicy = findDistanceFarePolicy(totalDistance);
        return distanceFarePolicy.calculateFare(totalDistance);
    }

    private DistanceFarePolicy findDistanceFarePolicy(int totalDistance) {
        return distanceFarePolicies.stream()
                .filter(distanceFarePolicy -> distanceFarePolicy.isSatisfiedBy(totalDistance))
                .findAny()
                .orElseThrow(NotSupportedDistanceFarePolicy::new);
    }
}
