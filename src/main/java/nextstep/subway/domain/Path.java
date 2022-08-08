package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.domain.fare.FareRule;

import java.util.List;

@Getter
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

    public int extractFare() {
        int distance = extractDistance();
        FareRule fareRule = FareRule.of(distance);
        return fareRule.getFare(distance);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
