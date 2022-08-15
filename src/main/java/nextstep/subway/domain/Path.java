package nextstep.subway.domain;

import nextstep.subway.domain.policy.PathByFare;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public Fare extractFare() {
        PathByFare pathByFare = generatePathByFare();
        return Fare.chaining().calculate(pathByFare);
    }

    private PathByFare generatePathByFare() {
        return PathByFare.builder()
                .distance(sections.totalDistance())
                .lines(extractLines())
                .build();
    }

    private List<Line> extractLines() {
        return sections.values()
                .stream()
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
