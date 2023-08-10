package nextstep.domain;

import nextstep.domain.Sections;
import nextstep.domain.Station;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Long extractDistance() {
        return sections.totalDistance();
    }

    public Long extractDuration() {
        return sections.totalDuration();
    }


}
