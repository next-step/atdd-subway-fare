package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    private final int additionalFareByLine;

    public Path(List<Line> lines, Sections sections) {
        this.sections = sections;
        this.additionalFareByLine = lines.stream().filter(sections::isInLine).map(Line::getAdditionalFare).reduce(0, Integer::max);
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

    public int getAdditionalFareByLine() {
        return additionalFareByLine;
    }
}
