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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractFare(int userAge) {
        final Age age = Age.findAge(userAge);
        return age.ageFare(Fare.calculateOverFare(extractDistance()));
    }

    public int maximumPrice() {
        return sections.maximumPrice();
    }

}
