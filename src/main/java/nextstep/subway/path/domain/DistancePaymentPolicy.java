package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

import static nextstep.subway.path.domain.PaymentPolicy.*;

@Component
public class DistancePaymentPolicy implements PaymentPolicy {
    public static final int COST_DELIMITER_50 = 8;
    public static final int COST_DELIMITER_10 = 5;


    @Override
    public Cost cost(PathResult pathResult) {
        int distance = pathResult.getTotalDistance();
        if (distance > 50) {
            int distanceOver50 = distance - 50;
            int discountOver10AndUnder50 = distance - distanceOver50 - 10;
            return new Cost(sum(calculateOverFare(distanceOver50, COST_DELIMITER_50) + calculateOverFare(discountOver10AndUnder50, COST_DELIMITER_10)));
        }
        if (distance > 10) {
            return new Cost(sum(calculateOverFare(distance - 10, COST_DELIMITER_10)));
        }
        return new Cost(DEFAULT_COST);
    }
}
