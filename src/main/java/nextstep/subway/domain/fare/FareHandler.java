package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public abstract class FareHandler {
    private FareHandler fareHandler;

    public FareHandler(FareHandler fareHandler) {
        this.fareHandler = fareHandler;
    }

    public Fare handle(int distance) {
        if (fareHandler != null) {
            return fareHandler.handle(distance);
        }
        return Fare.ZERO;
    }

}
