package nextstep.subway.domain.fee;

public class MiddleDistanceFee extends DistanceFee {

    public MiddleDistanceFee(int distance) {
        super(distance);
    }

    @Override
    protected int calculateOverFee(int distance) {
        int overFeeDistance = distance - 11;
        return calculateOverFee(overFeeDistance, 5);
    }
}
