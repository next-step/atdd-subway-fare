package nextstep.subway.domain.path.fee;

public class DistanceCalculateHandler extends CalculateHandler {

    public DistanceCalculateHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public void handle(FeeInfo pathInfo) {
        final Distance distance = pathInfo.distance();
        final DistanceRange distanceRange = DistanceRange.fromDistance(distance);

        super.fare = distanceRange.calculate(distance);
        super.handle(pathInfo);
    }
}
