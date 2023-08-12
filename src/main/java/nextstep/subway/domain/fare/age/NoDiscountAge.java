package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class NoDiscountAge implements DiscountAge {

    @Override
    public boolean isTarget(int age) {
        return age >= 19;
    }

    @Override
    public int discount(int fare) {
        return fare;
    }
}
