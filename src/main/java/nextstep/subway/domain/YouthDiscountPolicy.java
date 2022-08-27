package nextstep.subway.domain;

public class ChildrenDiscountPolicy extends DistanceFarePolicy {

    public ChildrenDiscountPolicy(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE;
    }

}
