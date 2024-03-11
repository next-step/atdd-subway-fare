package nextstep.subway.domain.farecalculation;

public class FareCalculation {

    private static final int BASE_FARE = 1250;

    public static int getFareByDistance(Long distance) {
        int additionalFare = 0;

        if (distance <= 10) {
            return BASE_FARE;
        } else if (distance <= 50) {
            additionalFare = calculationDistance50Under(distance - 10L);
        } else {
            additionalFare = calculationDistance50Under(40L)
                    + calculationDistance50Over(distance - 50L);
        }

        return BASE_FARE + additionalFare;
    }

    private static int calculationDistance50Under(Long distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int calculationDistance50Over(Long distance) {
        return ((int) (Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}
