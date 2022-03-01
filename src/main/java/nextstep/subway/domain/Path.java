package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareUtil;
import nextstep.subway.domain.fare.Over10Fare;
import nextstep.subway.domain.fare.Over50Fare;

import java.util.List;

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

    public int extractFare() {
        Over50Fare over50Fare = new Over50Fare();
        Over10Fare over10Fare = new Over10Fare();
        over50Fare.setNextChain(over10Fare);
        return FareUtil.getFare(over50Fare, sections.totalDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
