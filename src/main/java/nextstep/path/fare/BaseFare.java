package nextstep.path.fare;

public class BaseFare implements Fare {
    @Override
    public int calculate(int distance) {
        return 1250;
    }
}
