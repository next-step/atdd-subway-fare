package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class DefaultLinePolicy implements FarePolicy {
    @Override
    public int calculate() {
        return 0;
    }
}
