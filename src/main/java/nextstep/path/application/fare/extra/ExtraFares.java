package nextstep.path.application.fare.extra;

import nextstep.path.application.fare.extra.distance.DistanceExtraFareChain;
import nextstep.path.application.fare.extra.distance.FirstDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.distance.SecondDistanceExtraFareHandler;
import nextstep.path.application.fare.extra.line.LineExtraHandler;
import nextstep.path.domain.Path;

public class ExtraFares {

    private final DistanceExtraFareChain distanceExtraFareChain;
    private final LineExtraHandler lineExtraHandler;

    public ExtraFares() {
        this.distanceExtraFareChain = new DistanceExtraFareChain()
                .addNext(new FirstDistanceExtraFareHandler())
                .addNext(new SecondDistanceExtraFareHandler());
        this.lineExtraHandler = new LineExtraHandler();
    }

    public long calculateExtra(final Path path) {
        final long calculateByDistance = distanceExtraFareChain.calculate(path.getDistance());
        final long calculateByLines = lineExtraHandler.calculate(path.getUsedLine());
        return calculateByDistance + calculateByLines;
    }
}
