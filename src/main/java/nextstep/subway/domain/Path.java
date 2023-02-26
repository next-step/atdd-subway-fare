package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    private final int totalDistance;
    private final int totalDuration;
    private final int totalFare;

    public Path(Sections sections, int totalDistance, int totalDuration, int totalFare) {
        this.sections = sections;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalFare = totalFare;
    }

    public Sections getSections() {
        return sections;
    }
    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
