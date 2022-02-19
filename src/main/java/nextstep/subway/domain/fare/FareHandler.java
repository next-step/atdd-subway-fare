package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

import java.util.ArrayList;
import java.util.List;

public class FareHandler {

    private final List<FarePolicy> policies = new ArrayList<>();

    public FareHandler() {
        this.policies.add(DistancePolicy.getInstance());
        this.policies.add(ExtraChargePolicy.getInstance());
        this.policies.add(DiscountPolicy.getInstance());
    }

    public Fare calculate(FareParams fareParams) {
        Fare fare = Fare.free();
        for (FarePolicy policy : policies) {
            fare = policy.apply(fare, fareParams);
        }
        return fare;
    }

}
