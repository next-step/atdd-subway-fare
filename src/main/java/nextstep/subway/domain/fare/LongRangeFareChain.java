package nextstep.subway.domain.fare;

public class LongRangeFareChain implements FareChain {

    private static final int BIG_RANGE = 50;
    private static final int BIG_RANGE_PER_DISTANCE = 8;
    private static final int ADDITIONAL_FEE_PER_DISTANCE = 100;

    @Override
    public void setNextChain(FareChain fareChain) {
    }

    @Override
    public int calculateFare(int distance, int additionalFee, Integer age) {
        return (((distance - BIG_RANGE - 1) / BIG_RANGE_PER_DISTANCE) + 1) * ADDITIONAL_FEE_PER_DISTANCE;
    }
}
