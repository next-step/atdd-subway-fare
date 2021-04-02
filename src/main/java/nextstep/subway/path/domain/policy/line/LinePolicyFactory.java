package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class LinePolicyFactory {
    public static final int ZERO_FARE = 0;

    public static FarePolicy findPolicy(int lineFare) {
        if (lineFare > ZERO_FARE) {
            return new AdditionalLinePolicy(lineFare);
        }
        return new DefaultLinePolicy();
    }
}
