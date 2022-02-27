package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

@Component
public class SubwayMap {
    private SimpleDirectedWeightedGraph<Station, SectionEdge> graph;

    public SubwayMap() {
    }

    public void createSubwayMapGraph(List<Line> lines) {
        if (graph == null) {
            this.graph = createGraph(lines);
        }
    }

    public Path findPath(Station source, Station target, PathType pathType, String startTimeString) {
        AllKShortestPaths paths = findAllKShortestPaths(source, target);

        ShortestPaths shortestPaths = paths.getShortestPathsFrom(startTimeString);

        if (isSearchingShortestDistancePath(pathType)) {
            return Path.of(new Sections(shortestPaths.getShortestDistanceSections())
                , shortestPaths.getShortestDistance(), shortestPaths.getShortestDistanceArrivalTime());
        }

        return Path.of(new Sections(shortestPaths.getShortestDurationSections())
            , shortestPaths.getShortestDistance(), shortestPaths.getShortestDurationArrivalTime());
    }

    private boolean isSearchingShortestDistancePath(PathType pathType) {
        return pathType.equals(PathType.DISTANCE);
    }

    protected AllKShortestPaths findAllKShortestPaths(Station source, Station target) {
        return new AllKShortestPaths(new KShortestPaths(graph, 100), source, target);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(List<Line> lines) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph
            = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph, lines);
        addWeights(graph, lines);

        return graph;
    }

    private void addWeights(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, List<Line> lines) {
        addEdgeDistanceWeight(graph, lines);
        addOppositeEdgeDistanceWeight(graph, lines);
    }


    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, List<Line> lines) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, List<Line> lines) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addOppositeEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, List<Line> lines) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                    it.getLine(),
                    it.getDownStation(),
                    it.getUpStation(),
                    it.getDistance(),
                    it.getDuration()
                ))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }
}
