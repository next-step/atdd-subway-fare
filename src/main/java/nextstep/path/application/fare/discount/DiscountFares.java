package nextstep.path.application.fare.discount;

import nextstep.path.application.fare.discount.age.AgeDiscountHandler;
import nextstep.path.application.fare.discount.age.AgeRange;
import nextstep.path.application.fare.discount.age.FareDiscountInfo;

public class DiscountFares {

    private final AgeDiscountHandler ageDiscountHandler;

    public DiscountFares() {
        this.ageDiscountHandler =
                AgeDiscountHandler.of(AgeRange.of(6, 13), FareDiscountInfo.of(350, 0.5))
                        .next(AgeDiscountHandler.of(AgeRange.of(13, 19), FareDiscountInfo.of(350, 0.2)));
    }


    public long discount(final long fare, final Integer age) {
        return ageDiscountHandler.discount(fare, age);
    }
}
