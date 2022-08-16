package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.domain.Sections;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubwayMap {
    private final Map<PathType, DijkstraShortestPath<Long, SectionEdge>> graphCache = new EnumMap<>(PathType.class);
    private final List<Long> vertexes;
    private final List<Section> edges;

    public SubwayMap(List<Line> lines) {
        this.vertexes = extractVertexes(lines);
        this.edges = extractEdges(lines);
    }

    private List<Long> extractVertexes(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Section> extractEdges(List<Line> lines) {
        List<Section> allEdges = lines.stream()
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());

        List<Section> oppositeEdges = lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(Section::flip)
                .collect(Collectors.toList());

        allEdges.addAll(oppositeEdges);

        return allEdges;
    }

    public Path findPath(Long source, Long target, PathType type) {
        DijkstraShortestPath<Long, SectionEdge> shortestPathGraph = createGraphOfType(type);
        GraphPath<Long, SectionEdge> result = shortestPathGraph.getPath(source, target);

        if (result == null) {
            return Path.emptyPath();
        }

        List<Section> pathSections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(pathSections));
    }

    private DijkstraShortestPath<Long, SectionEdge> createGraphOfType(PathType type) {
        if (graphCache.containsKey(type)) {
            return graphCache.get(type);
        }

        SimpleDirectedWeightedGraph<Long, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        vertexes.forEach(graph::addVertex);
        edges.stream()
                .map(type::mapToEdge)
                .forEach(it -> {
                    graph.addEdge(it.source(), it.target(), it);
                    graph.setEdgeWeight(it, it.weight());
                });

        DijkstraShortestPath<Long, SectionEdge> resultGraph = new DijkstraShortestPath<>(graph);
        graphCache.put(type, resultGraph);
        return resultGraph;
    }

    public int shortestDistance(Long source, Long target) {
        DijkstraShortestPath<Long, SectionEdge> shortestPathGraph = createGraphOfType(PathType.DISTANCE);
        GraphPath<Long, SectionEdge> result = shortestPathGraph.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Sections(sections).totalDistance();
    }
}