package nextstep.subway.domain;

import java.util.List;
import nextstep.subway.domain.farechain.DistanceOverFare;
import nextstep.subway.domain.farechain.OverFarePolicyHandler;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFare() {

        OverFarePolicyHandler chain = new DistanceOverFare(null);
        Fare fare = new Fare(chain);

        return fare.charge(this);
    }
}
