package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Set;

public class Lines {
    private final Set<Line> lines;

    public Lines(Set<Line> lines) {
        this.lines = lines;
    }

    public int findMaxAdditionalFee() {
        return lines.stream()
                .mapToInt(Line::getAdditionalFee)
                .max()
                .orElse(0);
    }

    public Set<Line> getLines() {
        return Collections.unmodifiableSet(lines);
    }
}
