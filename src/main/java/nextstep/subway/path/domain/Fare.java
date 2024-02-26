package nextstep.subway.path.domain;

public enum Fare {

    UP_TO_10(0, 10, 0, 0),
    FROM_10_TO_50(10, 50, 5, 100),
    OVER_50(50, Integer.MAX_VALUE, 8, 100);

    private final int startDistance;
    private final int endDistance;
    private final int distanceUnit;
    private final int unitFare;

    Fare(int startDistance,
         int endDistance,
         int distanceUnit,
         int unitFare) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.distanceUnit = distanceUnit;
        this.unitFare = unitFare;
    }

    public int getStartDistance() {
        return startDistance;
    }

    public int getEndDistance() {
        return endDistance;
    }

    public int getDistanceUnit() {
        return distanceUnit;
    }

    public int getUnitFare() {
        return unitFare;
    }
}
