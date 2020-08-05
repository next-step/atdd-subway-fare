package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;

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
}
