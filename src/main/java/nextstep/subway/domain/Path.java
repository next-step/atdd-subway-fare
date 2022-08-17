package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Line> getPassingLines() {
        return sections.getPassingLines();
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.getDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
