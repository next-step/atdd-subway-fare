package nextstep.subway.domain.fare;

public class FareUtil {

    private static final int BASE_FARE = 1250;

    private FareUtil() {
    }

    public static int getFare(FareChain fareChain, int distance) {
        int overFare = fareChain.calculate(distance);
        return BASE_FARE + overFare;
    }

}
