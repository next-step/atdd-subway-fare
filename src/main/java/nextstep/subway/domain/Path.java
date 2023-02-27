package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int shortestDistance;
    private int shortestDuration;

    public Path(final Sections sections, final int shortestDistance, final int shortestDuration) {
        this.sections = sections;
        this.shortestDistance = shortestDistance;
        this.shortestDuration = shortestDuration;
    }

    public static Path from(final Sections sections) {
        return new Path(sections, sections.totalDistance(), sections.totalDuration());
    }

    public Sections getSections() {
        return sections;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public int getShortestDuration() {
        return shortestDuration;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
