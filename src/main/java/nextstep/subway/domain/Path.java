package nextstep.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {
    private final Sections sections;
    private final List<Station> stations;

    public Path(List<Section> sections, List<Station> stations) {
        this.sections = new Sections(sections);
        this.stations = stations;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int pathTotalDistance() {
        return sections.pathTotalDistance();
    }

    public int pathTotalDuration() {
        return sections.pathTotalDuration();
    }

    public int fare() {
        return sections.fare();
    }

    public Sections getSections() {
        return sections;
    }
}
