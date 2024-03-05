package nextstep.path.application.fare.discount.age;

import java.util.Objects;

public class NoneDiscountHandler extends AgeDiscountHandler {
    @Override
    protected boolean isInRange(final Integer age) {
        return Objects.isNull(age);
    }

    @Override
    protected long discountFare(final long fare) {
        return fare;
    }
}
