package nextstep.subway.path.domain;

public enum OverAddFareCalculate {

    TEN_OVER(10, 5),
    FIFTH_OVER(50, 8);

    private static final int OVER_FARE = 100;
    private static final int NOT_OVER_FARE = 0;
    private final int overDistance;
    private final int overFare;

    public int getOverDistance() {
        return overDistance;
    }

    public int getOverFare() {
        return overFare;
    }

    OverAddFareCalculate(int overDistance, int overFare) {
        this.overDistance = overDistance;
        this.overFare = overFare;
    }

    public static int calculate(int distance) {
        if (distance > TEN_OVER.overDistance && distance <= FIFTH_OVER.overDistance) {
            return TEN_OVER.calculateOverFare(distance - TEN_OVER.overDistance);
        }

        if (distance > FIFTH_OVER.overDistance) {
            return TEN_OVER.calculateOverFare(distance - TEN_OVER.overDistance)
                    + FIFTH_OVER.calculateOverFare(distance - FIFTH_OVER.overDistance);
        }

        return NOT_OVER_FARE;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / overFare)) * OVER_FARE);
    }
}
