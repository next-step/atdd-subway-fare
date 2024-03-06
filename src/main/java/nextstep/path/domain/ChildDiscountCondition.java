package nextstep.path.domain;

import java.math.BigDecimal;

public class ChildDiscountCondition implements DiscountCondition {

    private static final int DEDUCT_FARE = 350;
    private static final BigDecimal discountRatio = new BigDecimal("0.5");

    @Override
    public boolean support() {
        return true;
    }

    @Override
    public int discount(int originFare) {
        return new BigDecimal(originFare - DEDUCT_FARE).multiply(discountRatio).intValue();
    }
}
