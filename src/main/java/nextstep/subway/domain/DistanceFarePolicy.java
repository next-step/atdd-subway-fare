package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy {

    public static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;
    private static final int FIRST_CHARGED_PER_DISTANCE = 5;
    private static final int SECOND_CHARGED_PER_DISTANCE = 8;

    @Override
    public int calculator(final int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 0 이하일 수 없습니다.");
        }

        if (distance > SECOND_DISTANCE) {
            return DEFAULT_FARE
                    + calculateOverFare(SECOND_DISTANCE - FIRST_DISTANCE, FIRST_CHARGED_PER_DISTANCE)
                    + calculateOverFare(distance - SECOND_DISTANCE, SECOND_CHARGED_PER_DISTANCE);
        }

        if (distance > FIRST_DISTANCE) {
            return DEFAULT_FARE
                    + calculateOverFare(distance - FIRST_DISTANCE, FIRST_CHARGED_PER_DISTANCE);
        }

        return DEFAULT_FARE;
    }

    private int calculateOverFare(final int distance,
                                  final int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * ADDITIONAL_FARE);
    }
}
