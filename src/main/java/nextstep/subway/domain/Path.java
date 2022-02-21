package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int shortestDistance;

    public Path(Sections sections) {
        this(sections, sections.totalDistance());
    }

    private Path(Sections sections, int shortestDistance) {
        this.sections = sections;
        this.shortestDistance = shortestDistance;
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

    public int extractFare() {
        return PathFare.extractFare(shortestDistance);
    }

    public Path changeShortestDistancePath(Path shortestDistancePath) {
        return new Path(this.sections, shortestDistancePath.extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
