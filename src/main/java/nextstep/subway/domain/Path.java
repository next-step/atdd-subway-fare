package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int fareDistance;
    private int fare;
    private FareCalculator fareCalculator;

    protected Path() {
    }

    public Path(Sections sections, int fareDistance) {
        this.sections = sections;
        this.fareDistance = fareDistance;
    }

    public static Path of(Sections sections, int fareDistance) {
        return new Path(sections, fareDistance);
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

    public void calculateFare(int age) {
        this.fare = fareCalculator.calculateFare(fareDistance, sections, age);
    }

    public int extractFare() {
        return fare;
    }

    public void setFareCalculator(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }
}
