package nextstep.path.application.fare.discount;

import nextstep.path.application.fare.discount.age.AgeDiscountHandler;
import nextstep.path.application.fare.discount.age.KidDiscountHandler;
import nextstep.path.application.fare.discount.age.NoneDiscountHandler;
import nextstep.path.application.fare.discount.age.TeenDiscountHandler;

public class DiscountFares {

    private final AgeDiscountHandler ageDiscountHandler;

    public DiscountFares() {
        this.ageDiscountHandler =
                new NoneDiscountHandler()
                        .next(new KidDiscountHandler())
                        .next(new TeenDiscountHandler());
    }


    public long discount(final long fare, final Integer age) {
        return ageDiscountHandler.discount(fare, age);
    }
}
