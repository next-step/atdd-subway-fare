package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class AdditionalLinePolicy implements FarePolicy {

    private final int lineFare;

    public AdditionalLinePolicy(int lineFare) {
        this.lineFare = lineFare;
    }

    @Override
    public int calculate() {
        return lineFare;
    }
}
