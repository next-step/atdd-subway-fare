package nextstep.path.fare;

public class LongFare implements Fare {
    @Override
    public int calculate(int distance) {
        return 1250 + (int) ((Math.ceil((distance - 51) / 8.0)) * 100);
    }
}
