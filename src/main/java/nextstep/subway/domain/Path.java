package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private final FareChain fareChain;

    public Path(Sections sections, FareChain fareChain) {
        this.sections = sections;
        this.fareChain =fareChain;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare() {
        return fareChain.calculateFare(sections.totalDistance());
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
