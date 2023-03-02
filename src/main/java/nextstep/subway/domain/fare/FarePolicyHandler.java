package nextstep.subway.domain.fare;

public abstract class FarePolicyHandler {

    protected FarePolicyHandler next;

    public final void next(FarePolicyHandler nextPolicyHandler) {
        this.next = nextPolicyHandler;
    }

    public final Fare apply(Fare fare) {
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
