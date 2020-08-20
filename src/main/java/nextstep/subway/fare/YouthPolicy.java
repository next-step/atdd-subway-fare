package nextstep.subway.fare;

public class YouthPolicy extends DiscountPolicy {

    public YouthPolicy(DiscountCondition... conditions) {
        super(conditions);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return (fare - 350) * 80 / 100;
    }

}
