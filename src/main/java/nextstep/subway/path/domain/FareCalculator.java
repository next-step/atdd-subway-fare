package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public class FareCalculator implements FareCalculation{
    private Discount discount;
    private DistanceFare distanceFare;

    public FareCalculator(){
    }

    @Override
    public Fare calculate(Fare base, Distance distance) {
        final Fare totalBaseFare = Fare.sum(base, distanceFare.calculate(distance));
        return discount.apply(totalBaseFare);
    }

    public void setBaseFareStrategy(DistanceFare distanceFare) {
        this.distanceFare = distanceFare;
    }

    public void setDiscountFareStrategy(Discount discount) {
        this.discount = discount;
    }
}
