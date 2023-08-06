package nextstep.subway.domain;

public class OverFareCalculator {

    private int overFareInterval;
    private int overFarePerInterval;

    public OverFareCalculator(int overFareInterval, int overFarePerInterval) {
        this.overFareInterval = overFareInterval;
        this.overFarePerInterval = overFarePerInterval;
    }

    public int calculateOverFare(int overDistance) {

        if (overDistance <= 0) {
            return 0;
        }

        return (int) ((Math.ceil((overDistance - 1) / overFareInterval) + 1) * overFarePerInterval);
    }
}
