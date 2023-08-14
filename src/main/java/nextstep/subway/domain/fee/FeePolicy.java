package nextstep.subway.domain.fee;

public abstract class FeePolicy {
    private FeePolicy nextFeePolicy;

    public FeePolicy setNext(FeePolicy nextFeePolicy) {
        FeePolicy feePolicy = this;
        while (feePolicy.nextFeePolicy != null) {
            feePolicy = feePolicy.nextFeePolicy;
        }
        feePolicy.nextFeePolicy = nextFeePolicy;
        return this;
    }

    public int getFee(int fee) {
        FeePolicy feePolicy = this;

        while (feePolicy != null) {
            fee = feePolicy.calculateFee(fee);
            feePolicy = feePolicy.nextFeePolicy;
        }

        return fee;
    }

    abstract protected int calculateFee(int fee);
}
