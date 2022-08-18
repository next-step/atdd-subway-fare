package nextstep.subway.domain;

public enum OverFareType {

    INCLUDE_OVER_FARE(10, 100, 5),
    EXCEED_OVER_FARE(50, 100, 8);

    private final int applyDistance;
    private final int perDistance;
    private final int addFare;

    OverFareType(int applyDistance, int addFare, int perDistance) {
        this.applyDistance = applyDistance;
        this.perDistance = perDistance;
        this.addFare = addFare;
    }

    public int getPerDistance() {
        return perDistance;
    }

    public int getAddFare() {
        return addFare;
    }

    public int getApplyDistance() {
        return applyDistance;
    }
}
