package nextstep.path;

public class BasicDistanceFare extends DistanceFare {

    public BasicDistanceFare(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return 1_250;
    }

}
