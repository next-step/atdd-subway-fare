package nextstep.subway.domain;

import nextstep.subway.domain.farechain.OverFarePolicyHandler;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final OverFarePolicyHandler handler;

    public Fare(OverFarePolicyHandler handler) {
        this.handler = handler;
    }

    public int charge(Path path) {
        return handler.chargeHandler(path, DEFAULT_FARE);
    }
}
