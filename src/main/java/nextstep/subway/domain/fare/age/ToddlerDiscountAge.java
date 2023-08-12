package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class ToddlerDiscountAge implements DiscountAge {

    @Override
    public boolean isTarget(int age) {
        return age <= 5;
    }

    @Override
    public int discount(int fare) {
        return 0;
    }
}
