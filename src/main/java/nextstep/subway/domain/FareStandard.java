package nextstep.subway.domain;

public enum FareStandard {
    STEP0(1, 0),
    STEP1(11, 5),
    STEP2(51, 8);

    private final int boundary;
    private final int distance;

    FareStandard(int boundary, int distance) {
        this.boundary = boundary;
        this.distance = distance;
    }

    public int getBoundary() {
        return boundary;
    }

    public int getDistance() {
        return distance;
    }

    public static FareStandard decide(int totalDistance) {
        if (totalDistance >= STEP2.boundary) {
            return STEP2;
        } else if (totalDistance >= STEP1.boundary) {
            return STEP1;
        }
        return STEP0;
    }

    public boolean isBasicStandard() {
        return this.equals(STEP0);
    }

    public int getBasicBoundary() {
        return STEP1.boundary - 1;
    }
}
