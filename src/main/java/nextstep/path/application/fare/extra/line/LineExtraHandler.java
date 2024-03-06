package nextstep.path.application.fare.extra.line;

import nextstep.line.domain.Line;

import java.util.List;

public class LineExtraHandler {
    public long calculate(final List<Line> lines) {
        return lines.stream().mapToLong(Line::getExtraFare).max().orElse(0);
    }
}
