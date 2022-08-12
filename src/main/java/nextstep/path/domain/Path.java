package nextstep.path.domain;

import nextstep.line.domain.Sections;

import java.util.List;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Long> getStations() {
        return sections.getStations();
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }
}
