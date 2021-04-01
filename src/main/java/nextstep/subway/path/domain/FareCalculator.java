package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.Optional;

public class FareCalculator implements FareCalculation{
    private Discount discount;
    private DistanceFare distanceFare;

    public FareCalculator(){
        this.discount = null;
        this.distanceFare = null;
    }

    @Override
    public Fare calculate(Fare base, Distance distance) {
        final Fare distFare = calculateDistanceFare(distance);
        return discountFare(Fare.sum(base, distFare));
    }

    private Fare discountFare(Fare totalBaseFare) {
        return Optional.ofNullable(discount)
                .map(spec -> spec.apply(totalBaseFare))
                .orElse(totalBaseFare);
    }

    private Fare calculateDistanceFare(Distance distance) {
        return Optional.ofNullable(distanceFare)
                                    .map(spec -> spec.calculate(distance))
                                    .orElse(Fare.of(DistanceFare.BASE_FARE));
    }

    public void setBaseFareStrategy(DistanceFare distanceFare) {
        this.distanceFare = distanceFare;
    }

    public void setDiscountFareStrategy(Discount discount) {
        this.discount = discount;
    }
}
