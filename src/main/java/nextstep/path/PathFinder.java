package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.exception.PathNotFoundException;
import nextstep.line.LineRepository;
import nextstep.line.section.Section;
import nextstep.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PathFinder {
    private final LineRepository lineRepository;

    private WeightedMultigraph<Station, DistanceDurationEdge> init(Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<Station, DistanceDurationEdge> graph = new WeightedMultigraph<>(DistanceDurationEdge.class);
        lineRepository.findAll().forEach(line -> {
            line.getSections().forEach(section -> {
                Station upStation = section.getUpstation();
                Station downStation = section.getDownstation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                DistanceDurationEdge edge = graph.addEdge(upStation, downStation);
                if (edge != null) {
                    int weight = weightExtractor.apply(section);
                    edge.setDistance(section.getDistance());
                    edge.setDuration(section.getDuration());
                    graph.setEdgeWeight(edge, weight);
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

    private Path findPath(Station source, Station target, Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<Station, DistanceDurationEdge> graph = init(weightExtractor);
        DijkstraShortestPath<Station, DistanceDurationEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DistanceDurationEdge> path = dijkstra.getPath(source, target);

        if (path == null) {
            throw new PathNotFoundException("가능한 경로가 존재하지 않습니다.");
        }

        List<Station> pathVertices = path.getVertexList();
        int totalDistance = path.getEdgeList().stream()
                .mapToInt(DistanceDurationEdge::getDistance)
                .sum();
        int totalDuration = path.getEdgeList().stream()
                .mapToInt(DistanceDurationEdge::getDuration)
                .sum();

        return new Path(pathVertices, totalDistance, totalDuration);
    }

    public boolean pathExists(Station source, Station target) {
        WeightedMultigraph<Station, DistanceDurationEdge> graph = init(Section::getDistance);
        DijkstraShortestPath<Station, DistanceDurationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DistanceDurationEdge> path = dijkstraShortestPath.getPath(source, target);
        return path != null;
    }
}
