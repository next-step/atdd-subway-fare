package nextstep.subway.domain;

public class BelowDistance extends DistanceFarePolicy {

    public BelowDistance(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return BELOW_DEFAULT_FARE;
    }

}
