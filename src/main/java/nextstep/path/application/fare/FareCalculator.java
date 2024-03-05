package nextstep.path.application.fare;

import nextstep.line.domain.Line;
import nextstep.path.application.fare.discount.age.AgeDiscountHandler;
import nextstep.path.application.fare.discount.age.KidDiscountHandler;
import nextstep.path.application.fare.discount.age.TeenDiscountHandler;
import nextstep.path.application.fare.extra.distance.DistanceExtraFareChain;
import nextstep.path.application.fare.extra.distance.FirstDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.distance.SecondDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.line.LineExtraHandler;

import java.util.List;

public class FareCalculator {
    private final DistanceExtraFareChain distanceExtraFareChain;
    private final LineExtraHandler lineExtraHandler;
    private final AgeDiscountHandler ageDiscountHandler;

    public FareCalculator() {
        this.distanceExtraFareChain = new DistanceExtraFareChain()
                .addNext(new FirstDistanceExtraFareHandler())
                .addNext(new SecondDistanceExtraFareHandler());
        this.lineExtraHandler = new LineExtraHandler();
        this.ageDiscountHandler = new KidDiscountHandler()
                .next(new TeenDiscountHandler());
    }

    public long calculate(final List<Line> usedLines, final int distance, final int age) {
        final long calculateByDistance = distanceExtraFareChain.calculate(distance);
        final long calculateByLines = lineExtraHandler.calculate(usedLines);
        return ageDiscountHandler.discount(calculateByDistance + calculateByLines, age);
    }
}
