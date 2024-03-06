package nextstep.path.application.fare.discount.age;

public class FareDiscountInfo {
    private final long deductionAmount;
    private final double discountRate;

    private FareDiscountInfo(final long deductionAmount, final double discountRate) {
        validate(deductionAmount, discountRate);
        this.deductionAmount = deductionAmount;
        this.discountRate = discountRate;
    }

    private void validate(final long deductionAmount, final double discountRate) {
        if (deductionAmount < 0) {
            throw new IllegalArgumentException("deductionAmount must be positive number");
        }
        if (1 < discountRate || discountRate < 0) {
            throw new IllegalArgumentException("discountRate must be between 0 and 1");
        }
    }

    public static FareDiscountInfo of(final int deductionAmount, final double discountRate) {
        return new FareDiscountInfo(deductionAmount, discountRate);
    }

    public long applyDiscount(final long fare) {
        return fare - (int) Math.ceil((fare - deductionAmount) * discountRate);
    }
}
