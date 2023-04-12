package nextstep.subway.domain;

public class FareCalcurator {

    private static final int BASE_FARE = 1250;
    private static final int MIDDLE_FULL_FARE = 800;

    public int calculate(int distance) {

        if (distance <= 10) {

            return BASE_FARE;
        } else if (distance <= 50) {
            distance -= 10;

            return BASE_FARE +  (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        } else {
            distance -= 50;

            return (int) BASE_FARE + MIDDLE_FULL_FARE + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
    }
}
