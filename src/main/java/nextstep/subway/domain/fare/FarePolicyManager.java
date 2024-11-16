package nextstep.subway.domain.fare;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarePolicyManager {

    private List<FarePolicy> farePolicies;

    public FarePolicyManager(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public Fare calculateFare(FarePolicyContext context, Fare fare) {
        for (FarePolicy farePolicy : farePolicies) {
            if (farePolicy.support(context)) {
                fare = farePolicy.invoke(context, fare);
            }
        }

        return fare;
    }
}
