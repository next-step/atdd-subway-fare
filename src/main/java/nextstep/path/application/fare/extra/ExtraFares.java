package nextstep.path.application.fare.extra;

import nextstep.line.domain.Line;
import nextstep.path.application.fare.extra.distance.DistanceExtraFareChain;
import nextstep.path.application.fare.extra.distance.FirstDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.distance.SecondDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.line.LineExtraHandler;

import java.util.List;

public class ExtraFares {

    private final DistanceExtraFareChain distanceExtraFareChain;
    private final LineExtraHandler lineExtraHandler;

    public ExtraFares() {
        this.distanceExtraFareChain = new DistanceExtraFareChain()
                .addNext(new FirstDistanceExtraFareHandler())
                .addNext(new SecondDistanceExtraFareHandler());
        this.lineExtraHandler = new LineExtraHandler();
    }

    public long calculateExtra(final List<Line> usedLines, final int distance) {
        final long calculateByDistance = distanceExtraFareChain.calculate(distance);
        final long calculateByLines = lineExtraHandler.calculate(usedLines);
        return calculateByDistance + calculateByLines;
    }
}
