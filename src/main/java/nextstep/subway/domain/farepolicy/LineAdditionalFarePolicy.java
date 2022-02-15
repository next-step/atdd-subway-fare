package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Path;

public class LineAdditionalFarePolicy implements FarePolicy {
    @Override
    public int calculate(Path path) {
        return 0;
    }
}
