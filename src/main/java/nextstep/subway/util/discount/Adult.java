package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class Adult implements DiscountAgePolicy {

    @Override
    public void discount(Fare fare) { }

    @Override
    public boolean support(int age) {
        return age >= 19;
    }
}
