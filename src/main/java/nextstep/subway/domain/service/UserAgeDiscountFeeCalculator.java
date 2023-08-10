package nextstep.subway.domain.service;

import java.math.BigDecimal;

public class UserAgeDiscountFeeCalculator {
    private static final BigDecimal DISCOUNT_RATE_OF_TEENAGER_USER = BigDecimal.valueOf(20);
    private static final BigDecimal DISCOUNT_RATE_OF_CHILDREN_USER = BigDecimal.valueOf(50);

    public BigDecimal calculateDiscountFee(BigDecimal totalFee, StationPathDiscountFeeContext context) {
        return BigDecimal.ZERO;
    }
}
