package nextstep.subway.domain.policy.discount;

import nextstep.subway.domain.policy.calculate.CalculateConditions;

public class AgeDistcountFarePolicy implements FareDiscountPolicy {

    private final int start;
    private final int end;
    private final float discountRate;
    private final int discountAmount;


    public AgeDistcountFarePolicy(int start, int end, float discountRate, int discountAmount) {
        this.start = start;
        this.end = end;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean isSuite(CalculateConditions conditions) {
        return conditions.getAge() >= start && conditions.getAge() < end;
    }

    @Override
    public int discount(int fare) {
            return (int) ((fare - discountAmount)*(1-discountRate));
    }

}
