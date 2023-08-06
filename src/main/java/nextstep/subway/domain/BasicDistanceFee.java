package nextstep.subway.domain;

public class BasicDistanceFee extends DistanceFee {
    @Override
    protected int calculateOverFee(int distance) {
        return 0;
    }
}
