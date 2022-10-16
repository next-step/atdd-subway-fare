package nextstep.subway.domain.fare.discount;

import nextstep.subway.domain.fare.discount.DiscountFarePolicy;

public class Default extends DiscountFarePolicy {

    public Default(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return getFare();
    }

}
