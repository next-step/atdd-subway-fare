package nextstep.subway.domain.fee;

public class LongDistanceFee extends DistanceFee {

    public LongDistanceFee(int distance) {
        super(distance);
    }

    @Override
    protected int calculateOverFee(int distance) {
        int overFeeDistance = distance - 51;
        return 800 + calculateOverFee(overFeeDistance, 8);
    }
}
