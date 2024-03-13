package nextstep.subway.path;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int OVER_50KM_FARE = 2150;
    private static final int SURCHARGE = 100;

    public static int calculate(int distance) {
        if (distance < 11) {
            return BASE_FARE;
        }

        if (distance < 51) {
            return under50KmOver10KmFareCalculate(distance);
        }

        return over50KmFareCalculate(distance);
    }

    private static int under50KmOver10KmFareCalculate(int distance) {
        int overDistance = (distance - 10) / 5;
        return (BASE_FARE + SURCHARGE) + (SURCHARGE * overDistance);
    }

    private static int over50KmFareCalculate(int distance) {
        int overDistance = (distance - 50) / 8;
        return OVER_50KM_FARE + (SURCHARGE * overDistance);
    }
}
