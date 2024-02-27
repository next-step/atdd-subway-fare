package nextstep.path.fare;

public class MediumFare implements Fare {
    @Override
    public int calculate(int distance) {
        return 1250 + (int) ((Math.ceil((distance - 11) / 5.0)) * 100);
    }
}
