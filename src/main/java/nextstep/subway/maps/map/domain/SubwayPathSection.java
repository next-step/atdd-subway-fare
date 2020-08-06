package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class SubwayPathSection {

    protected final Line line;
    private final List<LineStationEdge> lineStationEdges = new ArrayList<>();

    public SubwayPathSection(Line line) {
        this.line = line;
    }

    public void addLineStationEdge(LineStationEdge lineStationEdge) {
        lineStationEdges.add(lineStationEdge);
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public static SubwayPathSection createSubwayPathSection(Pair<Line, List<LineStationEdge>> sectionPair) {
        List<LineStationEdge> sectionLineStationEdges = sectionPair.getSecond();
        LineStationEdge lineStationEdge = sectionLineStationEdges.get(0);
        PathDirection direction = PathDirection.getDirection(lineStationEdge);
        return direction.createSection(sectionPair.getFirst(), sectionLineStationEdges);
    }

    public abstract Long getFirstStationId();

    public abstract Long getLastStationId();

    public abstract LocalTime getRideTime(LocalTime time);

    public abstract LocalTime getAlightTime(LocalTime time);
}
