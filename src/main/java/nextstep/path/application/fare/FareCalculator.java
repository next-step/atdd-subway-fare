package nextstep.path.application.fare;

import nextstep.line.domain.Line;
import nextstep.path.application.fare.discount.age.AgeDiscountHandler;
import nextstep.path.application.fare.discount.age.KidDiscountHandler;
import nextstep.path.application.fare.discount.age.TeenDiscountHandler;
import nextstep.path.application.fare.extra.distance.FareChain;
import nextstep.path.application.fare.extra.distance.FirstExtraFareHandler;
import nextstep.path.application.fare.extra.distance.SecondExtraFareHandler;
import nextstep.path.application.fare.extra.line.LineExtraHandler;

import java.util.List;

public class FareCalculator {
    private final FareChain fareChain;
    private final LineExtraHandler lineExtraHandler;
    private final AgeDiscountHandler ageDiscountHandler;

    public FareCalculator() {
        this.fareChain = new FareChain()
                .addNext(new FirstExtraFareHandler())
                .addNext(new SecondExtraFareHandler());
        this.lineExtraHandler = new LineExtraHandler();
        this.ageDiscountHandler = new KidDiscountHandler()
                .next(new TeenDiscountHandler());
    }

    public long calculate(final List<Line> usedLines, final int distance, final int age) {
        final long calculateByDistance = fareChain.calculate(distance);
        final long calculateByLines = lineExtraHandler.calculate(usedLines);
        return ageDiscountHandler.discount(calculateByDistance + calculateByLines, age);
    }
}
