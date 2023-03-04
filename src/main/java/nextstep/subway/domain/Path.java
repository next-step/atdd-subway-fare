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

    public int getLineAdditionalFare() {
        return sections.getAllPassingLines().stream()
            .max(Comparator.comparing(Line::getAdditionalFare))
            .map(Line::getAdditionalFare)
            .orElseThrow(RuntimeException::new);
    }
}
