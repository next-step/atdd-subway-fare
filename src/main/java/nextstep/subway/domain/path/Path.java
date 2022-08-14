package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public class Path {
    private final Sections sections;
    private final int shortDistance;

    public Path(Sections sections, int shortDistance) {
        this.sections = sections;
        this.shortDistance = shortDistance;
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
        return FareCalculator.calculator(shortDistance);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
