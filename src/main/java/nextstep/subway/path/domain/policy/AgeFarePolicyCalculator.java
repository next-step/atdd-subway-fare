package nextstep.subway.path.domain.policy;

import nextstep.subway.path.enums.AgeFarePolicy;

public class AgeFarePolicyCalculator extends FarePolicyCalculator {

    private final int age;

    public AgeFarePolicyCalculator(int age) {
        this.age = age;
    }

    @Override
    public int calculate(int total) {
        return (int) ((total - getAgeDeduction()) * (1 - getAgeDiscount()));
    }

    private AgeFarePolicy matchingAgeFarePolicy() {
        return AgeFarePolicy.getMatchingAgeFarePolicy(age);
    }

    public double getAgeDiscount() {
        return matchingAgeFarePolicy().getDiscount();
    }

    public int getAgeDeduction() {
        return matchingAgeFarePolicy().getDeduction();
    }
}
