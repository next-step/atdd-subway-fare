package nextstep.subway.path.domain;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int SECTION1_DISTANCE = 50;

    private FareCalculator() {
    }

    public static int getFare(int distance) {
        return BASE_FARE + calculateOverFareOfSection1(distance) + calculateOverFareOfSection2(distance);
    }

    private static int calculateOverFareOfSection1(int distance) {
        if (distance <= 10) {
            return 0;
        }

        int distanceOfSection1 = distance - BASE_DISTANCE;
        if (distanceOfSection1 > SECTION1_DISTANCE - BASE_DISTANCE) {
            distanceOfSection1 = SECTION1_DISTANCE - BASE_DISTANCE;
        }

        return (int) ((Math.ceil((distanceOfSection1 - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFareOfSection2(int distance) {
        if (distance <= 50) {
            return 0;
        }

        int distanceOfSection2 = distance - SECTION1_DISTANCE;
        return (int) ((Math.ceil((distanceOfSection2 - 1) / 8) + 1) * 100);
    }
}