package nextstep.subway.domain;

public class Fare {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int MAX_OVER_TEN_FARE = 800;

    private final int fare;

    public Fare(int distance) {
        this.fare = generateFare(distance);
    }

    public int getFare() {
        return fare;
    }

    private int generateFare(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return BASE_FARE;
        }
        int additionalFare = calculateFare(distance);
        return BASE_FARE + additionalFare;
    }

    // 맨처음 55 -> 파라미터로 45 넘어옴
    private int calculateFare(int distance) {
        int calculateOverTen = calculateOverTen(distance);
        int calculateOverFifty = calculateOverFifty(distance);
        return calculateOverTen + calculateOverFifty;
    }

    private int calculateOverTen(int distance) {
        int overTenDistance = distance - 10;
        int fare = (int) ((Math.ceil((double) overTenDistance / 5)) * ADDITIONAL_FARE);
        return Math.min(fare, MAX_OVER_TEN_FARE);
    }

    private int calculateOverFifty(int distance) {
        int overFiftyDistance = distance - 50;
        if (overFiftyDistance <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) overFiftyDistance / 8) * ADDITIONAL_FARE;
    }
}
