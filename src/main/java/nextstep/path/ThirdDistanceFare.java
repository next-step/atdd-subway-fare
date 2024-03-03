package nextstep.path;


public class ThirdDistanceFare extends DistanceFare {
    public ThirdDistanceFare(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        int remain = getRemain();
        if (remain == 0 || distance <= 50) {
            return 0;
        }
        return (int) ((Math.ceil((remain - 1) / 8) + 1) * 100);
    }

    private int getRemain() {
        return distance - 50;
    }
}
