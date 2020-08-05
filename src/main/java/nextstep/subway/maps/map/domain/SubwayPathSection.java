package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SubwayPathSection {

    private final Line line;
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

    public Long getFirstStationId() {
        if (this.lineStationEdges.isEmpty()) {
            throw new RuntimeException("sections are empty");
        }
        return this.lineStationEdges.get(0).getLineStation().getPreStationId();
    }

    public Long getLastStationId() {
        int lineStationSize = this.lineStationEdges.size();
        return this.lineStationEdges.get(lineStationSize - 1).getLineStation().getStationId();
    }

    public PathDirection getDirection() {
        return PathDirection.getDirection(this.lineStationEdges);
    }

    public LocalTime getRideTime(LocalTime time) {
        return null;
    }

    public LocalTime getAlightTime(LocalTime time) {
        return null;
    }
}
