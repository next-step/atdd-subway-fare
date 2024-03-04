package nextstep.path.application.fare.discount.age;

import java.util.Objects;

public abstract class AgeDiscountHandler {
    protected AgeDiscountHandler nextHandler;

    public AgeDiscountHandler next(final AgeDiscountHandler nextHandler) {
        if (Objects.isNull(this.nextHandler)) {
            this.nextHandler = nextHandler;
            return this;
        }
        this.nextHandler.next(nextHandler);
        return this;
    }

    protected abstract boolean isInRange(int age);

    protected abstract long discountFare(long fare);

    public long discount(final long fare, final int age) {
        if (isInRange(age)) {
            return discountFare(fare);
        }
        if (Objects.isNull(nextHandler)) {
            return fare;
        }
        return nextHandler.discount(fare, age);
    }

}
