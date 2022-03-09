package nextstep.subway.domain;

public class FareAgePolicy implements Policy {

    private Policy policy;

    @Override
    public void setNextPolicy(Policy nextPolicy) {
        this.policy = nextPolicy;
    }

    @Override
    public int getPolicyFare(int fare, int age) {
            FareAgeEnum fareAgeEnum = FareAgeEnum.valueOf(age);
            return fareAgeEnum.getFareAge(fare);
    }
}
