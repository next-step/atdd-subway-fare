package nextstep.subway.path.domain.fare;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceFarePolicies {
    private final List<DistanceFarePolicy> distanceFarePolicies;

    public DistanceFarePolicies(List<DistanceFarePolicy> distanceFarePolicies) {
        this.distanceFarePolicies = distanceFarePolicies;
    }
}
