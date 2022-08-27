package nextstep.subway.domain;

public class ChildrenDiscountPolicy extends AgeDiscountPolicy {

    public ChildrenDiscountPolicy(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return (int) ((getFare() - 350) * 0.5);
    }

}
