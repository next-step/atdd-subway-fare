package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.path.domain.Path;

public abstract class AdditionalFarePolicy implements FarePolicy {
    private final FarePolicy next;

    public AdditionalFarePolicy(FarePolicy next) {
        this.next = next;
    }

    @Override
    public int calculateFare(Path path) {
        int fare = next.calculateFare(path);
        return afterCalculated(path, fare);
    }

    abstract protected int afterCalculated(Path path, int fare);
}
