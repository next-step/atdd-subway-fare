package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

import java.util.ArrayList;
import java.util.List;

public class FareHandler {

    private final List<FarePolicy> policies = new ArrayList<>();

    public FareHandler() {
        this.policies.add(new DistancePolicy());
        this.policies.add(new ExtraChargePolicy());
        this.policies.add(new DiscountPolicy());
    }

    public Fare calculate(FareParams fareParams) {
        Fare fare = Fare.free();
        for (FarePolicy policy : policies) {
            fare = policy.apply(fare, fareParams);
        }
        return fare;
    }

}
