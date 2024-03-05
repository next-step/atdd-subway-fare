package nextstep.path.application.fare.discount.age;

public class KidDiscountHandler extends AgeDiscountHandler {
    @Override
    protected boolean isInRange(final Integer age) {
        return 6 <= age && age < 13;
    }

    @Override
    protected long discountFare(final long fare) {
        return fare - (int) Math.ceil((fare - 350) * (0.5));
    }
}
