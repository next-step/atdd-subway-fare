package nextstep.subway.domain.farechain.overfare;

public abstract class OverFarePolicyHandler {

    private OverFarePolicyHandler nextHandler;

    protected OverFarePolicyHandler(OverFarePolicyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public OverFarePolicyHandler chain(OverFarePolicyHandler nextHandler) {

        setNextHandler(nextHandler);
        return this;
    }

    private void setNextHandler(OverFarePolicyHandler nextHandler) {

        if (this.nextHandler == null) {
            this.nextHandler = nextHandler;
            return;
        }

        this.nextHandler.chain(nextHandler);
    }

    public int chargeHandler(int fare) {
        if (nextHandler != null) {
            fare = nextHandler.chargeOverFare(fare);
        }

        return fare;
    }

    protected abstract int chargeOverFare(int fare);
}
