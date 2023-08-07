package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    private final int additionalFare;

    public Path(List<Line> lines, Sections sections) {
        this.sections = sections;
        this.additionalFare = lines.stream().filter(sections::isInLine).map(Line::getAdditionalFare).reduce(0, Integer::max);
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

    public int getAdditionalFare() {
        return additionalFare;
    }
}
