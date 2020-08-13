package nextstep.subway.maps.map.application;

public class FareCalculator{

    public static final int BASIC_FARE = 1250;

    public int calculate(int distance, int extraFare) {
        int overDistance = distance - 10;
        if (overDistance > 0) {
            return BASIC_FARE + calculateOverFare(overDistance) + extraFare;
        }
        return BASIC_FARE + extraFare;
    }

    private int calculateOverFare(int distance) {
        if (distance > 50) {
            return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }


}
