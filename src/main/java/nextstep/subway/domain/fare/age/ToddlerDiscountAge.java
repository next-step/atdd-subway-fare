package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public class ToddlerDiscountAge implements DiscountAge {

    private static final int TODDLER_START_AGE = 0;
    private static final int TODDLER_END_AGE = 5;

    @Override
    public boolean isTarget(int age) {
        return age >= TODDLER_START_AGE && age <= TODDLER_END_AGE;
    }

    @Override
    public int discount(int fare) {
        return 0;
    }
}
