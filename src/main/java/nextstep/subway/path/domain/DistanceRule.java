package nextstep.subway.path.domain;

public enum DistanceRule {
    PER_5KM( 10, 50, 5),
    PER_8KM(50, Integer.MAX_VALUE, 8);

    final private Distance minDistance;
    final private Distance maxDistance;
    final private Distance unitDistance;

    private DistanceRule(int minDistance, int maxDistance, int unitDistance) {
        this.minDistance = new Distance(minDistance);
        this.maxDistance = new Distance(maxDistance);
        this.unitDistance = new Distance(unitDistance);
    }

    public Distance getUnitDistance() {
        return unitDistance;
    }

    public Distance getRestDistance(Distance distance) {
        if(maxDistance.minus(distance).toIntValue() == 0){ // maxDistance <= distance
            return maxDistance.minus(minDistance);
        } else {
            return distance.minus(minDistance);
        }
    }
}
