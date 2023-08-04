package nextstep.subway.domain;

import nextstep.subway.domain.enums.PathType;
import nextstep.subway.domain.vo.Path;
import nextstep.subway.domain.vo.Sections;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;
import java.util.stream.Collectors;

public class PathFinder {

    private final List<Section> sections;

    public PathFinder(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isValidPath(Station sourceStation, Station targetStation) {
        return initializePath(sourceStation, targetStation, PathType.DISTANCE).map(this::mapToPath).isPresent();
    }

    private Optional<GraphPath<Station, DefaultWeightedEdge>> initializePath(Station sourceStation, Station targetStation, PathType type) {
        if (Objects.equals(sourceStation, targetStation)) {
            return Optional.empty();
        }

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        addStationVertexes(graph);
        addSectionEdges(type, graph);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }

    public Path getShortestPath(Station sourceStation, Station targetStation, PathType type) {
        return initializePath(sourceStation, targetStation, type)
                .map(this::mapToPath)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Path mapToPath(GraphPath<Station, DefaultWeightedEdge> path) {
        List<Station> stations = path.getVertexList();
        Sections sectionsInPath = new Sections(sections.stream()
                .filter(section -> stations.contains(section.getUpStation()) && stations.contains(section.getDownStation()))
                .collect(Collectors.toSet()));
        return new Path(stations, sectionsInPath.sumOfDistance(), sectionsInPath.sumOfDuration());
    }

    private void addStationVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        sections.stream()
                .map(section -> Set.of(section.getUpStation(), section.getDownStation()))
                .flatMap(Collection::stream)
                .forEach(graph::addVertex);
    }

    private void addSectionEdges(PathType type, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), type.getValue(section)));
    }
}