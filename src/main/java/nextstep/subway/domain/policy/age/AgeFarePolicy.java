package nextstep.subway.domain.policy.age;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.policy.BasicFarePolicy;

public class AgeFarePolicy implements BasicFarePolicy {
    @Override
    public int calculate(int age, int fare, int distance, Path path) {
        AgeType policy = AgeType.createFarePolicy(age);
        return policy.calculate(fare);
    }
}
