package nextstep.subway.domain.policy.surcharge;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.policy.BasicFarePolicy;

public class SurchargePolicy implements BasicFarePolicy {
    @Override
    public int calculate(int age, int fare, int distance, Path path) {
        return fare + path.extractFare();
    }
}
