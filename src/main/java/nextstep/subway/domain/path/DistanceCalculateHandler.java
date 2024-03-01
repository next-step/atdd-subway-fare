package nextstep.subway.domain.path;

public class DistanceCalculateHandler extends CalculateHandler {

    public DistanceCalculateHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public void handle(final Distance distance) {
        final DistanceRange distanceRange = DistanceRange.fromDistance(distance);

        super.fare = super.fare.add(distanceRange.calculate(distance));
        super.handle(distance);
    }
}
