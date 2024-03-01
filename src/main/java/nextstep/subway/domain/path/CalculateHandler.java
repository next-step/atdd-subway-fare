package nextstep.subway.domain.path;

public abstract class CalculateHandler {

    private CalculateHandler nextCalculateHandler;
    protected Fare fare = new Fare(0);
    public CalculateHandler(final CalculateHandler calculateHandler) {
        this.nextCalculateHandler = calculateHandler;
    }

    public void handle(Distance distance) {
        if (nextCalculateHandler != null) {
            nextCalculateHandler.handle(distance);
        }
    }

    public Fare getFare() {
        return this.fare;
    }
}
