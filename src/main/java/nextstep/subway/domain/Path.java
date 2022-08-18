package nextstep.subway.domain;

import java.util.Comparator;
import java.util.List;

public class Path {

    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public boolean lineFareChargeable() {
        return lineFare() > 0;
    }

    public int lineFare() {
        return allLinesPassingBy().stream()
                .map(Line::getFare)
                .max(Comparator.comparing(x -> x))
                .orElseThrow(() -> new IllegalStateException("추가 요금을 구할 수 없습니다."));
    }

    private List<Line> allLinesPassingBy() {
        return sections.allLinesPassingBy();
    }
}
