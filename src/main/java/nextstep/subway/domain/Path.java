package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareStrategy;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() { return sections.getTotalDuration(); }

    public int calculateFare(int distance) {
        FareStrategy fareStrategy = FareType.findStrategy(distance);
        return fareStrategy.calculate(distance);
    }
}
