package nextstep.subway.domain;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Path {
    private Sections sections;

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
}
