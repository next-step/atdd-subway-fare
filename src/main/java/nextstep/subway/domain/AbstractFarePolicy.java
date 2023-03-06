package nextstep.subway.domain;

public abstract class AbstractFarePolicy implements FarePolicy {

    protected FarePolicy nextPolicy;

    protected AbstractFarePolicy(FarePolicy nextPolicy) {
        this.nextPolicy = nextPolicy;
    }

    protected boolean hasNext() {
        return this.nextPolicy != null;
    }
}
