package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.discount.AgeDiscount;
import nextstep.subway.path.domain.specification.distance.FirstDistanceFare;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public class FareCalculator {

    private DistanceFare distanceFare;
    private Discount fareDiscount;

    public FareCalculator() {
        this.distanceFare = new FirstDistanceFare();
        this.fareDiscount = new AgeDiscount();
    }

    public Fare calculate(Fare baseFare, Distance distance, DiscountCondition condition) {
        final Fare totalBaseFare = Fare.sum(baseFare, distanceFare.calculate(distance));
        return fareDiscount.discount(totalBaseFare, condition);
    }
}
