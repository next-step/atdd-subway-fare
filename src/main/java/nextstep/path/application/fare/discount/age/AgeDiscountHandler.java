package nextstep.path.application.fare.discount.age;

import java.util.Objects;

public class AgeDiscountHandler {
    private final AgeRange ageRange;
    private final FareDiscountInfo fareDiscountInfo;
    private AgeDiscountHandler nextHandler;

    private AgeDiscountHandler(final AgeRange ageRange, final FareDiscountInfo fareDiscountInfo) {
        this.ageRange = ageRange;
        this.fareDiscountInfo = fareDiscountInfo;
    }

    public static AgeDiscountHandler of(final AgeRange ageRange, final FareDiscountInfo fareDiscountInfo) {
        return new AgeDiscountHandler(ageRange, fareDiscountInfo);
    }

    public AgeDiscountHandler next(final AgeDiscountHandler nextHandler) {
        if (Objects.isNull(this.nextHandler)) {
            this.nextHandler = nextHandler;
            return this;
        }
        this.nextHandler.next(nextHandler);
        return this;
    }

    public long discount(final long fare, final Integer age) {
        if (Objects.isNull(age)) {
            return fare;
        }
        if (isInRange(age)) {
            return discountFare(fare);
        }
        if (Objects.isNull(nextHandler)) {
            return fare;
        }
        return nextHandler.discount(fare, age);
    }

    private boolean isInRange(final int age) {
        return ageRange.contains(age);
    }

    private long discountFare(final long fare) {
        return fareDiscountInfo.applyDiscount(fare);
    }

}
