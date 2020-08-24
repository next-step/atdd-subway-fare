package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Long, LineStationEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addVertexWith(List<Line> lines) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStationInOrder().stream())
                .map(it -> it.getStationId())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> addVertex(it));
    }

    public void addEdge(List<Line> lines, PathType type) {
        // 지하철 역의 연결 정보(간선)을 등록
        for (Line line : lines) {
            line.getStationInOrder().stream()
                    .filter(it -> it.getPreStationId() != null)
                    .forEach(it -> addEdge(type, it, line));
        }
    }

    private void addEdge(PathType type, LineStation lineStation, Line line) {
        LineStationEdge lineStationEdge = new LineStationEdge(lineStation, line);
        addEdge(lineStation.getPreStationId(), lineStation.getStationId(), lineStationEdge);
        setEdgeWeight(lineStationEdge, type.findWeightOf(lineStation));
    }
}
