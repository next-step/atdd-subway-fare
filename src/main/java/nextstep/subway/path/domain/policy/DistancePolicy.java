package nextstep.subway.path.domain.policy;

public class DistancePolicy extends FarePolicy {

    private static final int OVER_FARE = 100;

    private Distance minDistance;
    private Distance maxDistance;
    private Distance unitDistance;
    private Distance inputDistance;

    public DistancePolicy(int minDistance, int maxDistance, int unitDistance, int inputDistance) {
        this.minDistance = new Distance(minDistance);
        this.maxDistance = new Distance(maxDistance);
        this.unitDistance = new Distance(unitDistance);
        this.inputDistance = new Distance(inputDistance);
    }

    @Override
    protected int calculate() {
        if (inputDistance.toInt() == 0) {
            return 0;
        }
        return (int) ((Math.ceil((getRestDistance(inputDistance).toInt() - 1) / unitDistance.toInt()) + 1) * OVER_FARE);
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
