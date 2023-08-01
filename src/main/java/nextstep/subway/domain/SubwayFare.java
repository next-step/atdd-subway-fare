package nextstep.subway.domain;

public class SubwayFare {
    private final int BASIC_FARE = 1250;
    private final int BASIC_DISTANCE = 10;
    private final int CHARGE_DISTANCE_FIVE = 5;
    private final int CHARGE_DISTANCE_EIGHT = 8;
    private final int ADDITIONAL_CHARGE_RATE = 100;

    public int calculateOverFare(int distance, int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * ADDITIONAL_CHARGE_RATE);
    }

    public int calculateFare(int distance) {
        validateDistance(distance);

        int fare = BASIC_FARE;

        if (distance > BASIC_DISTANCE && distance <= 50) {
            fare += calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE);
        }

        if (distance > 50) {
            fare += (calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE) +
                    calculateOverFare(distance - 50, CHARGE_DISTANCE_EIGHT));
        }

        return fare;
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리가 올바르지 않습니다.");
        }
    }
}
