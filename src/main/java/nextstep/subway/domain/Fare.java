package nextstep.subway.domain;

public class Fare {
    public static final int BASIC_FARE = 1250;
    public static final int BASIC_OVER_FARE = 100;

    private final int totalDistance;

    public Fare(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int impose() {
        FareStandard standard = FareStandard.decide(totalDistance);
        int overDistance = standard.calculateOverDistance(totalDistance);
        return BASIC_FARE + calculateOverFare(overDistance, standard);
    }

    private int calculateOverFare(int overDistance, FareStandard standard) {
        if (standard.isBasicStandard()) {
            return 0;
        }
        return (int) ((Math.ceil((overDistance - 1) / standard.getDistance()) + 1) * BASIC_OVER_FARE);
    }
}
