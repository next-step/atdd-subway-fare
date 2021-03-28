package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Cost;
import nextstep.subway.path.dto.CostRequest;

import static nextstep.subway.path.domain.policy.PaymentPolicy.*;

public class DistancePaymentPolicy implements PaymentPolicy {
    public static final int COST_DELIMITER_50 = 8;
    public static final int COST_DELIMITER_10 = 5;

    @Override
    public CostRequest cost(CostRequest costRequest) {
        int distance = costRequest.getDistance();
        if (distance > 50) {
            int distanceOver50 = distance - 50;
            int discountOver10AndUnder50 = distance - distanceOver50 - 10;
            Cost cost = new Cost(sum(calculateOverFare(distanceOver50, COST_DELIMITER_50) + calculateOverFare(discountOver10AndUnder50, COST_DELIMITER_10)));
            costRequest.updateCost(cost);
            return costRequest;
        }
        if (distance > 10) {
            Cost cost = new Cost(sum(calculateOverFare(distance - 10, COST_DELIMITER_10)));
            costRequest.updateCost(cost);
            return costRequest;
        }
        costRequest.updateCost(new Cost(DEFAULT_COST));
        return costRequest;
    }
}
