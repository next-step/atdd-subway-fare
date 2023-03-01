package nextstep.subway.domain;

public class OverFiftyFare implements Fare {

    private static final int OVER_FIFTY_MINIMUM_DISTANCE = 50;

    @Override
    public int calculateOverFare(int distance) {
        if (distance <= OVER_FIFTY_MINIMUM_DISTANCE) {
            return 0;
        }
        return (int) ((Math.ceil((distance - OVER_FIFTY_MINIMUM_DISTANCE - 1) / 8) + 1) * 100);
    }
}
