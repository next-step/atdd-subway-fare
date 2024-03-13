package nextstep.subway.path.domain;

import nextstep.subway.Exception.ErrorCode;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private final List<Section> sections;

    public Path(List<Section> sections) {
        this.sections = sections;
    }

    public PathResponse shortestPath(Station sourceStation, Station targetStation, PathType type) {
        isSameStation(sourceStation, targetStation);

        SimpleWeightedGraph<Station, CustomWeightedEdge> graph = makeSimpleWeightedGraph(allStations(sections), sections, type);
        GraphPath<Station, CustomWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(graph, sourceStation, targetStation);

        if (shortestPath == null) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "연결되지 않은 역 정보입니다.");
        }

        int totalDistance = totalDistance(shortestPath);
        return new PathResponse(shortestPath.getVertexList(), totalDistance, totalDuration(shortestPath), FareCalculator.calculateFare(totalDistance));
    }

    private static void isSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "출발역과 도착역이 같습니다.");
        }
    }

    private static List<Station> allStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> section.stations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private static SimpleWeightedGraph<Station, CustomWeightedEdge> makeSimpleWeightedGraph(List<Station> stations, List<Section> sections, PathType type) {
        SimpleWeightedGraph<Station, CustomWeightedEdge> graph = new SimpleWeightedGraph<>(CustomWeightedEdge.class);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            CustomWeightedEdge edge = new CustomWeightedEdge(section.getDistance(), section.getDuration());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, type.isDistance() ? section.getDistance() : section.getDuration());
        });
        return graph;
    }

    public void isConnected(Station sourceStation, Station targetStation) {
        shortestPath(sourceStation, targetStation, PathType.DISTANCE);
    }

    private int totalDistance(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDistance).sum();
    }

    private int totalDuration(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDuration).sum();
    }
}
