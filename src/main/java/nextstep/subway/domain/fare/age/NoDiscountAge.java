package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class NoDiscountAge implements DiscountAge {

    private static final int NO_DISCOUNT_START_AGE = 19;

    @Override
    public boolean isTarget(int age) {
        return age >= NO_DISCOUNT_START_AGE;
    }

    @Override
    public int discount(int fare) {
        return fare;
    }
}
