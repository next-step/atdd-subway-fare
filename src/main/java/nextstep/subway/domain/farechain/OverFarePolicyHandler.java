package nextstep.subway.domain.farechain;

public abstract class OverFarePolicyHandler {

    private final OverFarePolicyHandler nextHandler;

    protected OverFarePolicyHandler(OverFarePolicyHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public int chargeHandler(int fare) {
        if (nextHandler != null) {
            fare += nextHandler.chargeOverFare();
        }

        return fare;
    }

    public abstract int chargeOverFare();
}
