package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class ChildDiscountAge implements DiscountAge {

    private final BigDecimal discountDeduction;

    public ChildDiscountAge(BigDecimal discountDeduction) {
        this.discountDeduction = discountDeduction;
    }

    @Override
    public boolean isTarget(int age) {
        return age >= 6 && age <= 12;
    }

    @Override
    public int discount(int fare) {
        return BigDecimal.valueOf(fare).subtract(discountDeduction).multiply(new BigDecimal("0.5")).add(discountDeduction).intValue();
    }
}
