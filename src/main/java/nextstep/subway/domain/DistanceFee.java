package nextstep.subway.domain;

public abstract class DistanceFee {
    public static final int FIXED_AMOUNT = 1250;

    protected int calculateOverFee(int distance, int referenceDistance) {
        return (int) ((Math.ceil(distance / referenceDistance) + 1) * 100);
    }

    public int calculateFee(int distance) {
        return FIXED_AMOUNT + calculateOverFee(distance);
    }

    abstract protected int calculateOverFee(int distance);

}
