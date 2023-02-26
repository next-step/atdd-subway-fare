package nextstep.subway.domain;

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
        return sections.sumByCondition(Section::getDistance);
    }

    public int extractDuration() {
        return sections.sumByCondition(Section::getDuration);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
