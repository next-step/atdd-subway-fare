package nextstep.subway.path.domain;

public class FareCalculator {
    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;

    public static int calculateFare(int totalDistance) {
        if (totalDistance < 11) {
            return BASIC_FARE;
        }

        int totalFare = BASIC_FARE;
        if (totalDistance < 51) {
            int distance = totalDistance - 10;
            totalFare += calculateAdditionalFare(distance, 5);
            return totalFare;
        }

        totalFare += 800;
        int distance = totalDistance - 50;
        totalFare += calculateAdditionalFare(distance, 8);
        return totalFare;
    }

    private static int calculateAdditionalFare(int distance, int unit) {
        return (int) ((Math.ceil((double) distance / unit)) * ADDITIONAL_FARE);
    }
}
