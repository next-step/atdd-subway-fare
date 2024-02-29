package nextstep.path.fare.distance;

public class LongDistanceFare implements DistanceFare {
    @Override
    public int calculate(int distance) {
        return 1250 + (int) ((Math.ceil((distance - 51) / 8.0)) * 100);
    }
}
