package nextstep.path.fare.distance;

public class MediumDistanceFare implements DistanceFare {
    @Override
    public int calculate(int distance) {
        return 1250 + (int) ((Math.ceil((distance - 11) / 5.0)) * 100);
    }
}
