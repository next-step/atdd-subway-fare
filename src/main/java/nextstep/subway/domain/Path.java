package nextstep.subway.domain;

import nextstep.subway.domain.fare.DistancePolicy;

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

    public int extractFare() {
        int distance = sections.totalDistance();
        return DistancePolicy.calculate(distance);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
