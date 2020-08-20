package nextstep.subway.fare;

public class ChildPolicy extends DiscountPolicy {

    public ChildPolicy(DiscountCondition... conditions) {
        super(conditions);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return  (fare - 350) * 50 / 100;
    }
}
