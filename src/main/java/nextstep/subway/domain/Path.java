package nextstep.subway.domain;

import java.util.List;
import nextstep.subway.domain.fare.Fare;

public class Path {
    private final Sections sections;
    private final Fare fare;

    public Path(Sections sections) {
        this.sections = sections;
        this.fare = new Fare(distance());
    }

    public Sections getSections() {
        return sections;
    }

    public int distance() {
        return sections.sumByCondition(Section::getDistance);
    }

    public int duration() {
        return sections.sumByCondition(Section::getDuration);
    }

    public int cost() {
        return fare.cost();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
