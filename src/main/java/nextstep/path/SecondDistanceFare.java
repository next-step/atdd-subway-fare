package nextstep.path;

public class SecondDistanceFare extends DistanceFare {

    public SecondDistanceFare(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        int remain = getRemain();
        if (remain == 0 || distance <= 10) {
            return 0;
        }
        return (int) ((Math.ceil((remain - 1) / 5) + 1) * 100);
    }

    private int getRemain() {
        if (distance > 50) {
            return 40;
        }
        return distance - 10;
    }
}
