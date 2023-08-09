package nextstep.subway.domain.farechain;

import nextstep.subway.domain.Path;

public class OverFarePolicyHandlerImpl extends OverFarePolicyHandler{

    public OverFarePolicyHandlerImpl(OverFarePolicyHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public int chargeOverFare(Path path) {
        return 0;
    }
}
