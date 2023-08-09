package nextstep.subway.domain;

public class BasicDistanceFee extends DistanceFee {

    private static final int NONE_OVER_FEE = 0;

    @Override
    protected int calculateOverFee(int distance) {
        return NONE_OVER_FEE;
    }
}
