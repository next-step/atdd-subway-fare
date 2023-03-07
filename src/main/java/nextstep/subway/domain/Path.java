package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareStrategyFactory;

import java.util.List;
import java.util.Optional;

public class Path {
    private final Sections sections;

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

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare(Optional<Integer> age) {
        int fare = sections.calculateFare();

        return age.map(a -> FareStrategyFactory.of(a).calculateFare(fare))
                .orElse(fare);
    }

    public int calculateFare(int age) {
        return calculateFare(Optional.of(age));
    }
}
