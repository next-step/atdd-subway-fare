package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class LineFarePolicy implements FarePolicy {
    private List<Line> lines;

    public LineFarePolicy(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public int calculateFareByPolicy(int subtotal) {
        subtotal += lines.stream()
                .mapToInt(Line::getAdditionalLineFare)
                .max()
                .orElse(0);

        return subtotal;
    }
}
