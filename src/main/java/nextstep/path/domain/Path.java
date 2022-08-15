package nextstep.path.domain;

import nextstep.line.domain.Sections;

import java.util.List;

public class Path {
    private final Sections sections;

    public static Path emptyPath() {
        return new Path(Sections.emptySections());
    }

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Long> getStations() {
        return sections.getStations();
    }

    public int getDistance() {
        return sections.totalDistance();
    }

    public int getDuration() {
        return sections.totalDuration();
    }
}
