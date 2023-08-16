package nextstep.subway.domain.farechain.discountfare;

public abstract class DiscountFarePolicyHandler {

    private DiscountFarePolicyHandler nextHandler;

    protected DiscountFarePolicyHandler() {}

    protected DiscountFarePolicyHandler(DiscountFarePolicyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public DiscountFarePolicyHandler chain(DiscountFarePolicyHandler nextHandler) {

        setNextHandler(nextHandler);
        return this;
    }

    private void setNextHandler(DiscountFarePolicyHandler nextHandler) {

        if (this.nextHandler == null) {
            this.nextHandler = nextHandler;
            return;
        }

        this.nextHandler.chain(nextHandler);
    }

    public int chargeHandler(int fare) {
        if (nextHandler != null) {
            fare = nextHandler.discountOverFare(fare);
        }

        return fare;
    }

    protected abstract int discountOverFare(int fare);
}
