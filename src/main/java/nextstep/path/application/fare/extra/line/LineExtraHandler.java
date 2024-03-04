package nextstep.path.application.fare.extra.line;

import nextstep.line.domain.Line;

import java.util.List;

public class LineExtraHandler {
    private final List<Line> lines;

    public LineExtraHandler(final List<Line> lines) {
        this.lines = lines;
    }

    public long calculate() {
        return lines.stream().mapToLong(Line::getExtraFare).max().orElse(0);
    }
}
