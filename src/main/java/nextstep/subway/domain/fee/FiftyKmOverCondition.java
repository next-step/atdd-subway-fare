package nextstep.subway.domain.fee;

public class FiftyKmOverCondition implements FeeCondition{

    @Override
    public boolean isInclude(int distance) {
        return MIN_REFERENCE_DISTANCE < distance;
    }

    @Override
    public int calculateFee(int distance) {
        return DEFAULT_FEE + (int) ((Math.ceil((distance - MAX_REFERENCE_DISTANCE - 1) / 8) + 1) * EXTRA_FEE);
    }
}
