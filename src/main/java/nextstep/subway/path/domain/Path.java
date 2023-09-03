package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.policy.fare.FarePolicy;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Lines lines;
    private final Sections sections;

    public Path(Lines lines, Sections sections) {
        this.lines = lines;
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

    public int calculateFare(FarePolicy farePolicy) {
        return farePolicy.calculateFare(this);
    }

    public List<Integer> getAdditionalFares() {
        return lines.getAdditionalFares();
    }
}
