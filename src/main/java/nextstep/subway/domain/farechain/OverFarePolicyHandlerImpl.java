package nextstep.subway.domain.farechain;

public class OverFarePolicyHandlerImpl extends OverFarePolicyHandler{

    public OverFarePolicyHandlerImpl(OverFarePolicyHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public int chargeOverFare(int fare) {
        return super.chargeHandler(0);
    }
}
