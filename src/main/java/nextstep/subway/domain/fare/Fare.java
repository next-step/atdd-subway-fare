package nextstep.subway.domain.fare;

public class Fare {

    private static final int BASE_FARE = 1250;

    public static int getFare(int distance) {
        Over50Fare over50Fare = new Over50Fare();
        Over10Fare over10Fare = new Over10Fare();
        over50Fare.setNextChain(over10Fare);
        int overFare = over50Fare.calculate(distance);
        return BASE_FARE + overFare;
    }

}
