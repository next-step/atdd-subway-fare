package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }
    public int extractDuration() {
        return sections.totalDuration();
    }
    public int extractLineFare() {return sections.lineFare();}
    public List<Station> getStations() {
        return sections.getStations();
    }


}
