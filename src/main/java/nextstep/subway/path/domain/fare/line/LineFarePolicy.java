package nextstep.subway.path.domain.fare.line;

import nextstep.subway.line.domain.Lines;

public interface LineFarePolicy {
    int calculateAdditionalFare(Lines lines);
}
