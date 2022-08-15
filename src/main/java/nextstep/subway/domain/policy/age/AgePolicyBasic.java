package nextstep.subway.domain.policy.age;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.policy.BasicFarePolicy;

public class AgePolicyBasic implements BasicFarePolicy {
    @Override
    public int calculate(int age, int fare, int distance, Path path) {
        return fare;
    }
}
