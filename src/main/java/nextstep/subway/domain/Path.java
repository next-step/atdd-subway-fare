package nextstep.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1_250;

    private static final int BASIC_FARE_KM = 10;
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractFare() {
        return BASIC_FARE + calculateOverFare(extractDistance() - BASIC_FARE_KM);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
