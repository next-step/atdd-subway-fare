package nextstep.subway.domain;

import java.util.Optional;
import nextstep.subway.domain.farechain.discountfare.DiscountFarePolicyHandler;
import nextstep.subway.domain.farechain.discountfare.DiscountFarePolicyHandlerImpl;
import nextstep.subway.domain.farechain.discountfare.MemberAgeDiscountFare;
import nextstep.subway.domain.farechain.overfare.DistanceOverFare;
import nextstep.subway.domain.farechain.overfare.LineOverFare;
import nextstep.subway.domain.farechain.overfare.OverFarePolicyHandler;
import nextstep.subway.domain.farechain.overfare.OverFarePolicyHandlerImpl;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final OverFarePolicyHandler overFarePolicyHandler;
    private final DiscountFarePolicyHandler discountFarePolicyHandler;

    public Fare(OverFarePolicyHandler overFarePolicyHandler,
        DiscountFarePolicyHandler discountFarePolicyHandler) {
        this.overFarePolicyHandler = overFarePolicyHandler;
        this.discountFarePolicyHandler = discountFarePolicyHandler;
    }

    public static Fare of(Path path, Optional<Integer> userAge) {

        return new Fare(getOverFareChain(path), getDiscountChain(userAge));

    }

    private static OverFarePolicyHandler getOverFareChain(Path path) {
        OverFarePolicyHandlerImpl handler = new OverFarePolicyHandlerImpl();

        return handler
            .chain(new DistanceOverFare(path.extractDistance()))
            .chain(new LineOverFare(path.getLines()));

    }

    private static DiscountFarePolicyHandler getDiscountChain(Optional<Integer> userAge) {

        DiscountFarePolicyHandler handler = new DiscountFarePolicyHandlerImpl();

        return handler
            .chain(new MemberAgeDiscountFare(userAge));
    }

    public int charge() {
        int overFare = overFarePolicyHandler.chargeHandler(DEFAULT_FARE);
        return discountFarePolicyHandler.chargeHandler(overFare);
    }
}
