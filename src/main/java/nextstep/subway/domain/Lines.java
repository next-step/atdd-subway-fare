package nextstep.subway.domain;

import java.util.List;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Line indexOf(Section section) {
        return lines.stream()
                .filter(item -> item.getSections().contains(section))
                .findFirst()
                .get();
    }
}
