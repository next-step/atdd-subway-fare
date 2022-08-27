package nextstep.subway.domain;

public class YouthDiscountPolicy extends AgeDiscountPolicy {

    public YouthDiscountPolicy(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return (int) ((getFare() - 350) * 0.8);
    }

}
