package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.age.AgeFarePolicy;
import nextstep.subway.path.domain.policy.line.LineFarePolicy;

public abstract class FarePolicyTemplate {

    private final LineFarePolicy lineFarePolicy;
    private final AgeFarePolicy ageFarePolicy;

    protected int fare;

    public FarePolicyTemplate(LineFarePolicy lineFarePolicy, AgeFarePolicy ageFarePolicy, int distanceToFare) {
        this.lineFarePolicy = lineFarePolicy;
        this.ageFarePolicy = ageFarePolicy;
        this.fare = distanceToFare;
    }

    public final void applyFarePolicy(int extraCharge) {
        applyLineFarePolicy(lineFarePolicy);
        addExtraCharge(extraCharge);
        applyAgeFarePolicy(ageFarePolicy);
    }

    public int getFare() {
        return fare;
    }

    protected abstract void applyLineFarePolicy(LineFarePolicy lineFarePolicy);

    protected abstract void applyAgeFarePolicy(AgeFarePolicy ageFarePolicy);

    protected abstract void addExtraCharge(int extraCharge);
}
