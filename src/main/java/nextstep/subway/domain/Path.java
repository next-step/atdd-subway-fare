package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareHandler;
import nextstep.subway.domain.fare.FareParams;

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

    public Fare fare(int age) {
        return new FareHandler().calculate(FareParams.of(this, age));
    }

    public int extraCharge() {
        List<Line> lines = sections.containsLines();
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .orElse(0);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
