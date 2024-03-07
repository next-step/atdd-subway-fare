package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.exception.PathNotFoundException;
import nextstep.line.LineRepository;
import nextstep.line.section.Section;
import nextstep.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PathFinder {
    private final LineRepository lineRepository;

    private WeightedMultigraph<Station, SectionEdge> initForSinglePath(Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        lineRepository.findAll().forEach(line -> {
            line.getSections().forEach(section -> {
                Station upStation = section.getUpstation();
                Station downStation = section.getDownstation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                SectionEdge edge = graph.addEdge(upStation, downStation);
                if (edge != null) {
                    int weight = weightExtractor.apply(section);
                    edge.setEdge(section);
                    graph.setEdgeWeight(edge, weight);
                }
            });
        });

        return graph;
    }

    private Multigraph<Station, SectionEdge> initForMultiplePaths() {
        Multigraph<Station, SectionEdge> graph = new Multigraph<>(SectionEdge.class);

        lineRepository.findAll().forEach(line -> {
            line.getSections().forEach(section ->  {
                Station upStation = section.getUpstation();
                Station downStation = section.getDownstation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                SectionEdge edge = graph.addEdge(upStation, downStation);
                if (edge != null) {
                    edge.setEdge(section);
                }
            });
        });

        return graph;
    }

    public Path findFastestPath(Station source, Station target) {
        return findPath(source, target, Section::getDuration);
    }

    public Path findShortestPath(Station source, Station target) {
        return findPath(source, target, Section::getDistance);
    }

    public List<Path> findMultipleShortestPath(Station source, Station target) {
        Multigraph<Station, SectionEdge> graph = initForMultiplePaths();

        List<GraphPath<Station, SectionEdge>> graphPaths = new KShortestPaths(graph, 1000).getPaths(source, target);
        List<Path> paths = new ArrayList<>();
        graphPaths.forEach(path->{
            paths.add(toPath(path));
        });

        return paths;
    }

    private Path findPath(Station source, Station target, Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<Station, SectionEdge> graph = initForSinglePath(weightExtractor);
        DijkstraShortestPath<Station, SectionEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstra.getPath(source, target);

        if (path == null) {
            throw new PathNotFoundException("가능한 경로가 존재하지 않습니다.");
        }

        return toPath(path);
    }

    public boolean pathExists(Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = initForSinglePath(Section::getDistance);
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        return path != null;
    }

    private Path toPath(GraphPath<Station, SectionEdge> graphPath) {
        List<Station> stations = graphPath.getVertexList();
        List<Section> sections = graphPath.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
        int totalDistance = graphPath.getEdgeList().stream()
                .mapToInt(SectionEdge::getDistance)
                .sum();
        int totalDuration = graphPath.getEdgeList().stream()
                .mapToInt(SectionEdge::getDuration)
                .sum();

        return new Path(sections, stations, totalDistance, totalDuration);
    }
}
