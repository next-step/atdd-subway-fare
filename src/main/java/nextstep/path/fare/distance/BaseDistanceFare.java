package nextstep.path.fare.distance;

public class BaseDistanceFare implements DistanceFare {
    private static final int BASE_FARE = 1250;
    @Override
    public int calculate(int distance) {
        return BASE_FARE;
    }
}
