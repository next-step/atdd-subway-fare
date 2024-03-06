package nextstep.path.domain.dto;


import nextstep.path.domain.SectionEdge;
import nextstep.station.domain.Station;

import java.util.List;

public class PathsDto {

    private final int distance;
    private final int duration;
    private final List<Station> paths;
    private final List<SectionEdge> sections;

    public PathsDto(int distance, int duration, List<Station> paths, List<SectionEdge> sections) {
        this.distance = distance;
        this.duration = duration;
        this.paths = paths;
        this.sections = sections;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public List<Station> getPaths() {
        return paths;
    }

    public List<SectionEdge> getSectionEdges() {
        return sections;
    }


}
