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

    public int fare() {
        return FareType.fare(extractDistance());
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
