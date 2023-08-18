package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.fare.DistanceFarePolicies;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class Path {
    private final Set<Line> lines;
    private final Sections sections;

    public Path(Set<Line> lines, Sections sections) {
        this.lines = lines;
        this.sections = sections;
    }

    public Set<Line> getLines() {
        return lines;
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
