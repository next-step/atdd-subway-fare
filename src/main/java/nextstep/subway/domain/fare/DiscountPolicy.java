package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class DiscountPolicy implements FarePolicy {

    @Override
    public Fare apply(Fare fare, FareParams fareParams) {
        return fare.discount(fareParams.getAge());
    }

}
