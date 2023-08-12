package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class ChildDiscountAge implements DiscountAge {

    private static final int CHILD_START_AGE = 6;
    private static final int CHILD_END_AGE = 12;
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.5");

    private final BigDecimal discountDeduction;


    public ChildDiscountAge(BigDecimal discountDeduction) {
        this.discountDeduction = discountDeduction;
    }

    @Override
    public boolean isTarget(int age) {
        return age >= CHILD_START_AGE && age <= CHILD_END_AGE;
    }

    @Override
    public int discount(int fare) {
        return BigDecimal.valueOf(fare).subtract(discountDeduction).multiply(DISCOUNT_RATE).add(discountDeduction).intValue();
    }
}
