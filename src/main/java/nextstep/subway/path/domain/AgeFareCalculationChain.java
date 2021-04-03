package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;

public class AgeFareCalculationChain implements FareCalculationChain {

    private static final int AGE_DISCOUNT_DEDUCTION = 350;

    private FareCalculationChain chain;

    @Override
    public void setNextChain(FareCalculationChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public Fare calculate(FareCalculationCriteria criteria, Fare fare) {
        LoginMember member = criteria.getMember();
        if (isChild(member)) {
            fare = new Fare((fare.getFare()-AGE_DISCOUNT_DEDUCTION)/2);
        }
        if (isAdolescent(member)) {
            fare = new Fare((fare.getFare()-AGE_DISCOUNT_DEDUCTION)/5);
        }
        return fare;
    }

    private boolean isChild(LoginMember member) {
        if (member == null) {
            return false;
        }
        return member.getAge() >= 6 && member.getAge() < 13;
    }

    private boolean isAdolescent(LoginMember member) {
        if (member == null) {
            return false;
        }
        return member.getAge() >= 13 && member.getAge() < 19;
    }

}
