package nextstep.subway.domain.path;

public class DistanceFarePolicy {

    private static final int BASIC_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;

    public static int calculatePrice(final int totalDistance) {
        if (totalDistance <= DEFAULT_DISTANCE) {
            return BASIC_FARE;
        }

        if (totalDistance <= 50) {
            return BASIC_FARE + calculateOverFareLessThan50km(totalDistance - DEFAULT_DISTANCE);
        }

        return BASIC_FARE
                + calculateOverFareLessThan50km(40)
                + calculateOverFareMoreThan50km(totalDistance - 50);
    }

    private static int calculateOverFareLessThan50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFareMoreThan50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
