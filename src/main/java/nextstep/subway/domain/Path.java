package nextstep.subway.domain;

import java.util.List;

public class Path {
    private PathType pathType;
    private Sections sections;

    public Path(PathType pathType, Sections sections) {
        this.pathType = pathType;
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public PathType getPathType() {
        return pathType;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public long extractTotalFare() {
        Fare fare = Fare.builder()
                .distance(sections.totalDistance())
                .overFareLine(sections.overFareLine())
                .build();

        return fare.getFare().longValue();
    }
}
