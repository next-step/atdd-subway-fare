package nextstep.core.subway.fare.application;

import nextstep.core.subway.path.application.FareCalculationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareCalculator {

    private final List<FareCalculatePolicy> fareCalculatePolicies;

    public FareCalculator(List<FareCalculatePolicy> fareCalculatePolicies) {
        this.fareCalculatePolicies = fareCalculatePolicies;
    }

    public int calculateTotalFare(FareCalculationContext context) {
        int totalFare = 0;
        for (FareCalculatePolicy policy : fareCalculatePolicies) {
            totalFare += policy.apply(context);
        }

        return totalFare;
    }
}
