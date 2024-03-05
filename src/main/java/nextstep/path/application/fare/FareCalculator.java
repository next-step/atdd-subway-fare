package nextstep.path.application.fare;

import nextstep.path.application.fare.discount.DiscountFares;
import nextstep.path.application.fare.extra.ExtraFares;
import nextstep.path.domain.Path;

public class FareCalculator {
    private final ExtraFares extraFares;
    private final DiscountFares discountFares;

    public FareCalculator() {
        this.extraFares = new ExtraFares();
        this.discountFares = new DiscountFares();
    }

    public long calculate(final Path path, final int age) {
        return discountFares.discount(extraFares.calculateExtra(path), age);
    }
}
