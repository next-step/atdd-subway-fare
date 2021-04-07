package nextstep.subway.path.domain.rule;

public enum DistancePolicyRule {

    FIRST_RULE(10, 50, 5),
    SECOND_RULE(50, Integer.MAX_VALUE, 8);

    private final int minDistance;
    private final int maxDistance;
    private final int unitDistance;

    DistancePolicyRule(int minDistance, int maxDistance, int unitDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.unitDistance = unitDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getUnitDistance() {
        return unitDistance;
    }
}
