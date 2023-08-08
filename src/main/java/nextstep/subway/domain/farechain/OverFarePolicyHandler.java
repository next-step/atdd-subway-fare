package nextstep.subway.domain.farechain;

import nextstep.subway.domain.Path;

public abstract class OverFarePolicyHandler {

    private final OverFarePolicyHandler nextHandler;

    protected OverFarePolicyHandler(OverFarePolicyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public int chargeHandler(Path path, int fare) {
        if (nextHandler != null) {
            fare += nextHandler.chargeHandler(path, fare);
        }

        return fare;
    }
}
