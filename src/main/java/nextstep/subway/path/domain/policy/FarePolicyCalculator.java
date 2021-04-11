package nextstep.subway.path.domain.policy;

public abstract class FarePolicyCalculator {

    private static final int BASIC_FARE = 1250;
    private FarePolicyCalculator next = null;
    protected int total = BASIC_FARE;

    public FarePolicyCalculator setNext(FarePolicyCalculator next) {
        this.next = next;
        return next;
    }

    public void support(int total) {
        this.total = calculate(total);
        if (next != null) {
            next.support(this.total);
        }
    }

    public int getTotal() {
        return this.total;
    }

    public abstract int calculate(int total);
}
