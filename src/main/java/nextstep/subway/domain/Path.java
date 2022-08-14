package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private Sections shortestDistanceSections;

    public Path(Sections sections, Sections shortestDistanceSections) {
        this.sections = sections;
        this.shortestDistanceSections = shortestDistanceSections;
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

    public Fare extractFare() {
        return Fare.chaining().calculate(shortestDistanceSections.totalDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
