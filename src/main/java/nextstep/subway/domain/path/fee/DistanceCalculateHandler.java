package nextstep.subway.domain.path.fee;

public class DistanceCalculateHandler extends CalculateHandler {

    public DistanceCalculateHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public Fare handle(FeeInfo pathInfo, Fare beforeFare) {
        final Distance distance = pathInfo.distance();
        final DistanceRange distanceRange = DistanceRange.fromDistance(distance);

        final Fare fare = distanceRange.calculate(distance);
        return super.handle(pathInfo, beforeFare.add(fare));
    }
}
