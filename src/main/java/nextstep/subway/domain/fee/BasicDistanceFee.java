package nextstep.subway.domain.fee;

public class BasicDistanceFee extends DistanceFee {

    private static final int NONE_OVER_FEE = 0;

    public BasicDistanceFee(int distance) {
        super(distance);
    }

    @Override
    protected int calculateOverFee(int distance) {
        return NONE_OVER_FEE;
    }
}
