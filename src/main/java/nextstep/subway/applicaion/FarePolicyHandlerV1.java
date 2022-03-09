package nextstep.subway.applicaion;

import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.Path;

import java.util.ArrayList;
import java.util.List;

public class FarePolicyHandlerV1 implements FarePolicyHandler {

    private final List<FarePolicy> policies = new ArrayList<>();

    @Override
    public int execute(int age, int requestFare, Path path) {
        int result = 0;
        for (FarePolicy policy : policies) {
            result = policy.fare(age, result, path);
        }
        return result;
    }

    @Override
    public FarePolicyHandler chain(FarePolicy farePolicy) {
        policies.add(farePolicy);
        return this;
    }
}
