package nextstep.subway.domain.fee;

public abstract class DistanceFee extends FeePolicy {

    private final int distance;

    public DistanceFee(int distance) {
        this.distance = distance;
    }

    protected int calculateOverFee(int distance, int referenceDistance) {
        return (int) ((Math.ceil(distance / referenceDistance) + 1) * 100);
    }

    @Override
    protected int calculateFee(int fee) {
        return fee + calculateOverFee(distance);
    }

    abstract protected int calculateOverFee(int distance);

}
