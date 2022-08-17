package nextstep.subway.domain.SubwayMap;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class DurationSubwayMap extends SubwayMap {

    public DurationSubwayMap(List<Line> lines) {
        super(lines);
    }

    @Override
    protected GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(getGraph());
        return dijkstraShortestPath.getPath(source, target);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> getGraph() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                                it.getLine(),
                                it.getDownStation(),
                                it.getUpStation(),
                                it.getDistance(),
                                it.getDuration()
                        )
                )
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
        return graph;
    }
}
