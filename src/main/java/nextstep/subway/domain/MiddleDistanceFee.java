package nextstep.subway.domain;

public class MiddleDistanceFee extends DistanceFee {

    @Override
    protected int calculateOverFee(int distance) {
        int overFeeDistance = distance - 11;
        return calculateOverFee(overFeeDistance, 5);
    }
}
