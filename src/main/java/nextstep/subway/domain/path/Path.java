package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.finder.FareCalculator;

public class Path {

    private final Sections sections;
    private final int shortDistance;
    private final int age;

    public Path(Sections sections, int shortDistance, int age) {
        this.sections = sections;
        this.shortDistance = shortDistance;
        this.age = age;
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
        return new FareCalculator().calculator(shortDistance, age, sections.getMaxLineFare());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
