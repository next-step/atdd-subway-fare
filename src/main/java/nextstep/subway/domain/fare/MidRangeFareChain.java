package nextstep.subway.domain.fare;

public class MidRangeFareChain implements FareChain{

    private static final int MID_RANGE_START = 10;
    private static final int MID_RANGE_END = 50;
    private static final int MID_RANGE_PER_DISTANCE = 5;
    private static final int ADDITIONAL_FEE_PER_DISTANCE = 100;
    private FareChain longRangeFareChain;

    @Override
    public void setNextChain(FareChain fareChain) {
        this.longRangeFareChain = fareChain;
    }

    @Override
    public int calculateFare(int distance) {
        if (distance > MID_RANGE_END) {
            return getFare(MID_RANGE_END) + longRangeFareChain.calculateFare(distance);
        }
        return getFare(distance);
    }

    private static int getFare(int distance) {
        return (((distance - MID_RANGE_START - 1) / MID_RANGE_PER_DISTANCE) + 1) * ADDITIONAL_FEE_PER_DISTANCE;
    }
}
