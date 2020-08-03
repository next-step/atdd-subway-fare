package nextstep.subway.maps.boarding.domain;

import nextstep.subway.maps.line.domain.Fare;
import org.springframework.stereotype.Component;

@Component
public class MinorsFareCalculationPolicy implements FareCalculationPolicy {

    private static final Integer DEDUCTION_AMOUNT = 350;

    @Override
    public int calculateFare(FareCalculationContext context) {
        final Age passengerAge = context.getPassengerAge();
        if (Age.ADULT.equals(passengerAge)) {
            return context.previousResult().getAmount();
        }

        final Fare fare = context.previousResult();
        final Fare deducted = fare.minus(new Fare(DEDUCTION_AMOUNT));
        final Fare totalFare = deducted.discountByRatio(passengerAge.getDiscountRate());
        final Integer result = totalFare.getAmount();
        context.setCalculationResult(result);
        return result;
    }
}
