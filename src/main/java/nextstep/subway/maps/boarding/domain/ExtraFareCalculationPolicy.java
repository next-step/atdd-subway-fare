package nextstep.subway.maps.boarding.domain;

import nextstep.subway.maps.line.domain.Fare;

public class ExtraFareCalculationPolicy implements FareCalculationPolicy {

    @Override
    public int calculateFare(FareCalculationContext context) {
        final Fare basicFare = context.previousResult();
        final Fare extraFare = context.getMaximumExtraFareOnBoarding();
        final Fare total = basicFare.plus(extraFare);
        final Integer fare = total.getAmount();
        context.setCalculationResult(fare);
        return fare;
    }
}
