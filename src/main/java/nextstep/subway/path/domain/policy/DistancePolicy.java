package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.rule.DistancePolicyRule;

public class DistancePolicy extends FarePolicy {

    private static final int OVER_FARE = 100;

    private Distance minDistance;
    private Distance maxDistance;
    private Distance unitDistance;
    private Distance inputDistance;

    public DistancePolicy(DistancePolicyRule distancePolicyRule, int inputDistance) {
        this.minDistance = new Distance(distancePolicyRule.getMinDistance());
        this.maxDistance = new Distance(distancePolicyRule.getMaxDistance());
        this.unitDistance = new Distance(distancePolicyRule.getUnitDistance());
        this.inputDistance = new Distance(inputDistance);
    }

    @Override
    protected void calculate() {
        if (inputDistance.toInt() != 0) {
            fare += (int) ((Math.ceil((getRestDistance(inputDistance).toInt() - 1) / unitDistance.toInt()) + 1) * OVER_FARE);
        }
    }

    @Override
    protected boolean isValidate() {
        return minDistance.isLessThan(inputDistance);
    }

    private Distance getRestDistance(Distance distance) {
        if (maxDistance.isLessThan(distance)) {
            return maxDistance.minus(minDistance);
        }
        return distance.minus(minDistance);
    }

}
