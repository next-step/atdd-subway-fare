package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FareRequest;
import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.Path;

import java.util.ArrayList;
import java.util.List;

public class FarePolicyHandlerV1 implements FarePolicyHandler {

    private final List<FarePolicy> policies = new ArrayList<>();

    @Override
    public int execute(FareRequest fareRequest, Path path) {
        for (FarePolicy policy : policies) {
            fareRequest = policy.fare(fareRequest, path);
        }
        return fareRequest.getFare();
    }

    @Override
    public FarePolicyHandler chain(FarePolicy farePolicy) {
        policies.add(farePolicy);
        return this;
    }
}
