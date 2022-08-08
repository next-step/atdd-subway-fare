package nextstep.subway.domain;

import java.util.List;

import static nextstep.subway.domain.Fare.calculate;

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

    public int extractFare(int userAge) {
        return calculate(
                extractDistance(),
                sections.getHigherSurCharge(),
                userAge);
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
