package nextstep.subway.domain;

public class DefaultFarePolicy extends DistanceFarePolicy {

    public DefaultFarePolicy(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE;
    }

}
