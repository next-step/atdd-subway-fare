package nextstep.subway.domain;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.line.Sections;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SubwayMap {
    List<Line> lines;

    public static SubwayMap create(List<Line> lines, PathType pathType) {
        if (pathType == PathType.DISTANCE) {
            return new SubwayDistanceMap(lines);
        }
        return new SubwayDurationMap(lines);
    }

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target) {

        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

        // 지하철 역의 연결 정보(간선)을 등록
        registerStations(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        registerSections(graph);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return Path.of(Sections.of(sections));
    }

    protected abstract void registerSections(SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

    protected abstract void registerStations(SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

}
