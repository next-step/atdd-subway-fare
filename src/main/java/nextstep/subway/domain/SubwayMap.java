package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType, String startTimeString) {
        AllKShortestPaths paths = findAllKShortestPaths(source, target, pathType);

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

    protected AllKShortestPaths findAllKShortestPaths(Station source, Station target, PathType pathType) {
        return new AllKShortestPaths(new KShortestPaths(createGraph(pathType), 100), source, target);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(PathType pathType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph
            = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addWeights(graph, pathType);

        return graph;
    }

    private void addWeights(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType pathType) {
        addEdgeDistanceWeight(graph);
        addOppositeEdgeDistanceWeight(graph);
    }


    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addOppositeEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
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
