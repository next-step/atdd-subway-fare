package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
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
}
