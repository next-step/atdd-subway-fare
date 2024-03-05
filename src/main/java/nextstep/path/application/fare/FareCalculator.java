package nextstep.path.application.fare;

import nextstep.line.domain.Line;
import nextstep.path.application.fare.discount.DiscountFares;
import nextstep.path.application.fare.extra.ExtraFares;

import java.util.List;

public class FareCalculator {
    private final ExtraFares extraFares;
    private final DiscountFares discountFares;

    public FareCalculator() {
        this.extraFares = new ExtraFares();
        this.discountFares = new DiscountFares();
    }

    public long calculate(final List<Line> usedLines, final int distance, final int age) {
        return discountFares.discount(extraFares.calculateExtra(usedLines, distance), age);
    }
}
