package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int distance() {
        return sections.sumByCondition(Section::getDistance);
    }

    public int duration() {
        return sections.sumByCondition(Section::getDuration);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Sections getSections() {
        return sections;
    }
}
