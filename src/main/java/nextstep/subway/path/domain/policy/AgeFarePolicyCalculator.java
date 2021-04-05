package nextstep.subway.path.domain.policy;

import nextstep.subway.path.enums.AgeFarePolicy;

public class AgeFarePolicyCalculator{

    private final int age;

    public AgeFarePolicyCalculator(int age) {
        this.age = age;
    }

    private AgeFarePolicy matchingAgeFarePolicy(){
        return AgeFarePolicy.getMatchingAgeFarePolicy(age);
    }

    public double getAgeDiscount(){
        return matchingAgeFarePolicy().getDiscount();
    }

    public int getAgeDeduction(){
        return matchingAgeFarePolicy().getDeduction();
    }
}
