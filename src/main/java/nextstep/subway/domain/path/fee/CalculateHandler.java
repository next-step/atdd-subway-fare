package nextstep.subway.domain.path.fee;

public abstract class CalculateHandler {

    private CalculateHandler nextCalculateHandler;

    public CalculateHandler(final CalculateHandler calculateHandler) {
        this.nextCalculateHandler = calculateHandler;
    }

    public Fare handle(FeeInfo pathInfo, Fare fare) {
        if (nextCalculateHandler != null) {
            return nextCalculateHandler.handle(pathInfo, fare);
        }

        return fare;
    }
}
