package nextstep.subway.domain;

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

    public int calculateFare(Age age) {
        var fare = Fare.calculate(extractDistance(), sections.calculateLineExtraFare());
        return age.discountFare(fare);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
