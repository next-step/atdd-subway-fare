package nextstep.path.application.fare.discount.age;

public class TeenDiscountHandler extends AgeDiscountHandler {
    @Override
    protected boolean isInRange(final int age) {
        return 13 <= age && age < 19;
    }

    @Override
    protected long discountFare(final long fare) {
        return fare - (int) Math.ceil((fare - 350) * (0.2));
    }
}
