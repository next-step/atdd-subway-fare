package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private final FareChain fareChain;
    private int extraFare;

    public Path(Sections sections, FareChain fareChain, int extraFare) {
        this.sections = sections;
        this.fareChain = fareChain;
        this.extraFare = extraFare;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare() {
        return fareChain.calculateFare(sections.totalDistance()) + extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
