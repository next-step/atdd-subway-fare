package nextstep.path.domain;

import java.math.BigDecimal;

public class TeenagerDiscountCondition implements DiscountCondition {
    private static final int DEDUCT_FARE = 350;
    private static final BigDecimal discountRatio = new BigDecimal("0.8");
    @Override
    public boolean support() {
        return true;
    }

    @Override
    public int discount(int originFare) {
        if (originFare < 0) {
            throw new IllegalArgumentException("origin fare should be greater than 0");
        }
        return new BigDecimal(originFare - DEDUCT_FARE).multiply(discountRatio).intValue();
    }
}
