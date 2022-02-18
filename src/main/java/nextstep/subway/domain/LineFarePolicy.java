package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class LineFarePolicy {

    private List<Line> lines = new ArrayList<>();

    public LineFarePolicy(final List<Line> lines) {
        this.lines = lines;
    }

    public int calculate() {
        return lines.stream().mapToInt(Line::getCharge).max().orElse(0);
    }
}
