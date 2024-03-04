package nextstep.subway.domain.path.fee;

public abstract class CalculateHandler {

    private CalculateHandler nextCalculateHandler;
    protected Fare fare = new Fare(0);
    public CalculateHandler(final CalculateHandler calculateHandler) {
        this.nextCalculateHandler = calculateHandler;
    }

    public void handle(FeeInfo pathInfo) {
        if (nextCalculateHandler != null) {
            nextCalculateHandler.handle(pathInfo);
        }
    }

    public Fare fare() {
        if (nextCalculateHandler != null) {
            return this.fare.add(nextCalculateHandler.fare());
        }

        return this.fare;
    }
}
