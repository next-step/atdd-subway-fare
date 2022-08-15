package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections shortestSectionsByPathType;
    private Sections shortestDistanceSections;

    public Path(Sections shortestSectionsByPathType, Sections shortestDistanceSections) {
        this.shortestSectionsByPathType = shortestSectionsByPathType;
        this.shortestDistanceSections = shortestDistanceSections;
    }

    public int extractDistance() {
        return shortestSectionsByPathType.totalDistance();
    }

    public int extractDuration() {
        return shortestSectionsByPathType.totalDuration();
    }

    public Fare extractFare() {
        return Fare.chaining().calculate(shortestDistanceSections.totalDistance());
    }

    public List<Station> getStations() {
        return shortestSectionsByPathType.getStations();
    }
}
