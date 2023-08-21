package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.discount.DiscountPolicy;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class Path {
    private final Lines lines;
    private final Sections sections;

    public Path(Lines lines, Sections sections) {
        this.lines = lines;
        this.sections = sections;
    }

    public Set<Line> getLines() {
        return lines.getLines();
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

    public int calculateFare(DistanceFarePolicies distanceFarePolicies, LineFarePolicy lineFarePolicy, DiscountPolicy discountPolicy) {
        int fare = distanceFarePolicies.calculateFare(getTotalDistance());
        int maxAdditionalFare = lineFarePolicy.calculateAdditionalFare(lines);

        int totalFare = fare + maxAdditionalFare;
        return discountPolicy.discount(totalFare);
    }
}
