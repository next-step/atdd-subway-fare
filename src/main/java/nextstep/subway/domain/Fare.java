package nextstep.subway.domain;

import java.util.Optional;
import nextstep.subway.domain.farechain.DistanceOverFare;
import nextstep.subway.domain.farechain.LineOverFare;
import nextstep.subway.domain.farechain.MemberAgeDiscountFare;
import nextstep.subway.domain.farechain.OverFarePolicyHandler;
import nextstep.subway.domain.farechain.OverFarePolicyHandlerImpl;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final OverFarePolicyHandler handler;

    public Fare(OverFarePolicyHandler handler) {
        this.handler = handler;
    }

    public static Fare of(Path path, Optional<Integer> userAge) {

        return userAge
            .map(integer -> new Fare(getChain(path, integer)))
            .orElseGet(() -> new Fare(getChainNonLoginUser(path)));

    }

    private static OverFarePolicyHandler getChain(Path path, int userAge) {
        return new OverFarePolicyHandlerImpl(
            new DistanceOverFare(
                new LineOverFare(
                    new MemberAgeDiscountFare(null, userAge), path.getLines()
                )
                , path.extractDistance()
            )
        );
    }

    private static OverFarePolicyHandler getChainNonLoginUser(Path path) {
        return new OverFarePolicyHandlerImpl(
            new DistanceOverFare(
                new LineOverFare(null, path.getLines()), path.extractDistance()
            )
        );
    }

    public int charge() {
        return handler.chargeHandler(DEFAULT_FARE);
    }
}
