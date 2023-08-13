package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class TeenDiscountAge implements DiscountAge {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.8");
    private static final int TEEN_START_AGE = 13;
    private static final int TEEN_END_AGE = 18;

    private final BigDecimal discountDeduction;

    public TeenDiscountAge(BigDecimal discountDeduction) {
        this.discountDeduction = discountDeduction;
    }

    @Override
    public boolean isTarget(int age) {
        return age >= TEEN_START_AGE && age <= TEEN_END_AGE;
    }

    @Override
    public int discount(int fare) {
        return BigDecimal.valueOf(fare).subtract(discountDeduction).multiply(DISCOUNT_RATE).add(discountDeduction).intValue();
    }
}
