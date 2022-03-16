package nextstep.subway.domain.fee;

public class FiftyKmUnderCondition implements FeeCondition{

    @Override
    public boolean isInclude(int distance) {
        return MIN_REFERENCE_DISTANCE < distance && distance <= MAX_REFERENCE_DISTANCE;
    }

    @Override
    public int calculateFee(int distance) {
        return DEFAULT_FEE + (int) ((Math.ceil((distance -MIN_REFERENCE_DISTANCE - 1) / 5) + 1) * EXTRA_FEE);
    }
}
