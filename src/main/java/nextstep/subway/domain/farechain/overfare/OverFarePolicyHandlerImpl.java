package nextstep.subway.domain.farechain.overfare;

public class OverFarePolicyHandlerImpl extends OverFarePolicyHandler{

    public OverFarePolicyHandlerImpl() {
        super(null);
    }

    @Override
    public int chargeOverFare(int fare) {
        return super.chargeHandler(0);
    }
}
