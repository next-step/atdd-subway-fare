package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class AdditionalLinePolicy implements FarePolicy {
    public AdditionalLinePolicy(int lineFare) {
    }

    @Override
    public int calculate() {
        return 0;
    }
}
