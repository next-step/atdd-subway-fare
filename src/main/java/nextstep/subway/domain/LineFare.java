package nextstep.subway.domain;

import java.util.Comparator;
import java.util.List;

public class LineFare {

    private List<Line> lines;

    public LineFare(List<Line> lines) {
        this.lines = lines;
    }

    public int get() {
        return lines.stream()
                .map(Line::getFare)
                .max(Comparator.comparing(x -> x))
                .orElseThrow(() -> new IllegalStateException("추가 요금을 구할 수 없습니다."));
    }
}
