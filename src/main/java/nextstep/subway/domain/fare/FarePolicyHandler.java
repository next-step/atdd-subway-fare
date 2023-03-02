package nextstep.subway.domain.fare;

public abstract class FarePolicyHandler {

    protected FarePolicyHandler next;

    public void next(FarePolicyHandler nextPolicyHandler) {
        this.next = nextPolicyHandler;
    }

    public Fare apply(Fare fare) {
        Fare execute = execute(fare);

        if (hasNext()) {
            return next.apply(execute);
        }

        return execute;
    }

    private boolean hasNext() {
        return next != null;
    }

    public abstract Fare execute(Fare fare);
}
