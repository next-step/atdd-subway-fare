package nextstep.subway.domain.policy;

public abstract class FarePolicy {
    private FarePolicy farePolicy;

    public FarePolicy() {
        this(null);
    }

    public FarePolicy(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculate() {
        if (farePolicy != null) {
            return farePolicy.calculate();
        }
        return 0;
    }
}
