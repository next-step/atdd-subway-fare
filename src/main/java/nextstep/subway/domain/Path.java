package nextstep.subway.domain;

import java.util.List;

public class Path {

    private final Sections sections;
    private final Fare fare;

    public Path(Sections sections) {
        this.sections = sections;
        fare = new Fare();
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

    public int calcFare() {
        return fare.calcFare();
    }

    public void addPolicy(FarePolicy farePolicy) {
        fare.addPolicy(farePolicy);
    }

}
