package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public interface DiscountAgePolicy {

    int DEDUCTION_FARE = 350;

    void discount(Fare fare);

    boolean support(int age);
}
