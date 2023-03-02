package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    private final SectionEdges sectionEdges;

    public Path(Sections sections, SectionEdges sectionEdges) {
        this.sections = sections;
        this.sectionEdges = sectionEdges;
    }

    public Sections getSections() {
        return sections;
    }
    public List<Station> getStations() {
        return sections.getStations();
    }

    public SectionEdges getSectionEdges() {
        return sectionEdges;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Line> getLines() {
        return sectionEdges.getLines();
    }
}
