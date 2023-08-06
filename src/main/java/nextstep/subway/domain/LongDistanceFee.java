package nextstep.subway.domain;

public class LongDistanceFee extends DistanceFee {

    @Override
    protected int calculateOverFee(int distance) {
        int overFeeDistance = distance - 51;
        return 800 + calculateOverFee(overFeeDistance, 8);
    }
}
