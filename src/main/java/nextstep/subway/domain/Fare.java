package nextstep.subway.domain;

import java.util.Optional;
import nextstep.subway.domain.farechain.DistanceOverFare;
import nextstep.subway.domain.farechain.LineOverFare;
import nextstep.subway.domain.farechain.OverFarePolicyHandler;
import nextstep.subway.domain.farechain.OverFarePolicyHandlerImpl;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final OverFarePolicyHandler handler;

    public Fare(OverFarePolicyHandler handler) {
        this.handler = handler;
    }

    public static Fare of(Path path, Optional<Integer> userAge) {

        return new Fare(getChain(path, userAge.orElse(0)));

    }

    private static OverFarePolicyHandler getChain(Path path, int userAge) {
        OverFarePolicyHandlerImpl handler = new OverFarePolicyHandlerImpl();

        return handler
            .chain(new DistanceOverFare(path.extractDistance()))
            .chain(new LineOverFare(path.getLines()));

    }

    public int charge() {
        return handler.chargeHandler(DEFAULT_FARE);
    }
}
