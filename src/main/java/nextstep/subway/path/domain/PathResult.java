package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResult {
    private Sections sections;
    private int age;

    public PathResult(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public int getTotalFare() {
        final AgeDiscount ageDiscount = new AgeDiscount(age);
        int totalFare = getLineMaxBaseFare() + new DistanceFare().calculate(getTotalDistance());
        return ageDiscount.calculate(totalFare);

    }

    private Integer getLineMaxBaseFare() {
        return sections.getLineMaxFare();
    }
}
