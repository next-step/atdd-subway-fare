package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.rule.AgePolicyRule;

public class AgePolicy extends FarePolicy {

    private static final int DISCOUNT_FARE = 350;

    private Integer minAge;
    private Integer maxAge;
    private Double discountRate;
    private Integer inputAge;

    public AgePolicy(AgePolicyRule agePolicyRule, int inputAge) {
        this.minAge = agePolicyRule.getMinAge();
        this.maxAge = agePolicyRule.getMaxAge();
        this.discountRate = agePolicyRule.getDiscountRate();
        this.inputAge = inputAge;
    }

    @Override
    protected void calculate() {
        fare = (int) Math.floor((fare - DISCOUNT_FARE) * discountRate);
    }

    @Override
    protected boolean isValidate() {
        return (minAge <= inputAge) && (inputAge < maxAge);
    }

}
