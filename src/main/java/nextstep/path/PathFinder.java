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

    private WeightedMultigraph<Station, SectionEdge> init(Function<Section, Integer> weightExtractor) {
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
                    edge.setDistance(section.getDistance());
                    edge.setDuration(section.getDuration());
                    edge.setSection(section);
                    graph.setEdgeWeight(edge, weight);
                }
            });
        });

        return graph;
    }

    private Multigraph<Station, SectionEdge> initForDeparture() {
        Multigraph<Station, SectionEdge> graph = new Multigraph<>(SectionEdge.class);

        lineRepository.findAll().forEach(line -> {
            line.getSections().forEach(section ->  {
                Station upStation = section.getUpstation();
                Station downStation = section.getDownstation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                SectionEdge edge = graph.addEdge(upStation, downStation);
                if (edge != null) {
                    //int weight = weightExtractor.apply(section);
                    edge.setDistance(section.getDistance());
                    edge.setDuration(section.getDuration());
                    edge.setSection(section);
                    //graph.setEdgeWeight(edge, weight);
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

    public List<Path> findKShortestPath(Station source, Station target) {
        Multigraph<Station, SectionEdge> graph = initForDeparture();

        List<GraphPath<Station, SectionEdge>> graphPaths = new KShortestPaths(graph, 1000).getPaths(source, target);
        List<Path> paths = new ArrayList<>();
        graphPaths.forEach(path->{
            List<Station> stations = path.getVertexList();
            List<Section> sections = path.getEdgeList().stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());
            int totalDistance = path.getEdgeList().stream()
                    .mapToInt(SectionEdge::getDistance)
                    .sum();
            int totalDuration = path.getEdgeList().stream()
                    .mapToInt(SectionEdge::getDuration)
                    .sum();

            paths.add(new Path(sections, stations, totalDistance, totalDuration));
        });

        return paths;
    }


    private Path findPath(Station source, Station target, Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<Station, SectionEdge> graph = init(weightExtractor);
        DijkstraShortestPath<Station, SectionEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstra.getPath(source, target);

        if (path == null) {
            throw new PathNotFoundException("가능한 경로가 존재하지 않습니다.");
        }

        List<Station> stations = path.getVertexList();
        List<Section> sections = path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
        int totalDistance = path.getEdgeList().stream()
                .mapToInt(SectionEdge::getDistance)
                .sum();
        int totalDuration = path.getEdgeList().stream()
                .mapToInt(SectionEdge::getDuration)
                .sum();

        return new Path(sections, stations, totalDistance, totalDuration);
    }

    public boolean pathExists(Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = init(Section::getDistance);
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        return path != null;
    }
}
