package nextstep.subway.domain;

import nextstep.subway.domain.fare.Fare;

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

    public Fare calculateFare(int memberAge) {
        return sections.totalFare(memberAge);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
