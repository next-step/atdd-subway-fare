package nextstep;

import nextstep.subway.domain.policy.FareManager;
import nextstep.subway.domain.policy.FarePolicy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class FareManagerLoader {

    private final List<FarePolicy> farePolicies;

    public FareManagerLoader(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    @PostConstruct
    public void initialize() {
        FareManager.clearPolicy();

        for (FarePolicy farePolicy : farePolicies) {
            FareManager.addPolicy(farePolicy);
        }
    }
}
