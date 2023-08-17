package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fare.DistanceFarePolicies;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return sections.calculateTotalDistance();
    }

    public int getTotalDuration() {
        return sections.calculateTotalDuration();
    }

    public int calculateFare(DistanceFarePolicies distanceFarePolicies) {
        int totalDistance = getTotalDistance();
        return distanceFarePolicies.calculateFare(totalDistance);
    }
}
