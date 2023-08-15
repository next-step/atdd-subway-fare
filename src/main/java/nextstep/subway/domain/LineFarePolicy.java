package nextstep.subway.domain;

import java.util.List;

public class LineFarePolicy implements FarePolicy {
    private final List<Line> lines;

    public LineFarePolicy(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public int calculate() {
        return lines.stream()
                .mapToInt(Line::getFare)
                .max()
                .orElse(0);
    }
}
