package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> extractLineNames() {
        return sections.getStations().stream().map(Station::getName).collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
