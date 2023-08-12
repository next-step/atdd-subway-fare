package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class TeenDiscountAge implements DiscountAge {

    private final BigDecimal discountDeduction;

    public TeenDiscountAge(BigDecimal discountDeduction) {
        this.discountDeduction = discountDeduction;
    }

    @Override
    public boolean isTarget(int age) {
        return age >= 13 && age <= 18;
    }

    @Override
    public int discount(int fare) {
        return BigDecimal.valueOf(fare).subtract(discountDeduction).multiply(new BigDecimal("0.8")).add(discountDeduction).intValue();
    }
}
