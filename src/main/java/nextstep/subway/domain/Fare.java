package nextstep.subway.domain;

public class Fare {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int BASE_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int MAX_OVER_TEN_FARE = 800;

    private int fare;

    public Fare() {}

    public void init(int distance) {
        this.fare = generateFare(distance);
    }

    public int getFare() {
        return fare;
    }

    private int generateFare(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return BASE_FARE;
        }
        int additionalFare = new CalculateFareFacade(distance).calculateFare();
        return BASE_FARE + additionalFare;
    }
}
