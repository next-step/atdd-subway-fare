package nextstep.subway.path.domain;

import nextstep.subway.Exception.ErrorCode;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private final List<Line> lines;
    private final int BASIC_FARE = 1_250;
    private final int ADDITIONAL_FARE = 100;


    public Path(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse shortestPath(Station sourceStation, Station targetStation, PathType type) {
        isSameStation(sourceStation, targetStation);

        List<Section> sections = allSections();
        SimpleWeightedGraph<Station, CustomWeightedEdge> graph = makeSimpleWeightedGraph(allStations(sections), sections, type);
        GraphPath<Station, CustomWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(graph, sourceStation, targetStation);

        if (shortestPath == null) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "연결되지 않은 역 정보입니다.");
        }
        int totalDistance = totalDistance(shortestPath);
        return new PathResponse(shortestPath.getVertexList(), totalDistance, totalDuration(shortestPath), totalFare(totalDistance));
    }

    private int totalFare(int totalDistance) {
        if (totalDistance < 11) {
            return BASIC_FARE;
        }

        int totalFare = BASIC_FARE;
        if (totalDistance < 51) {
            int distance = totalDistance - 10;
            totalFare += calculateAdditionalFare(distance, 5);
            return totalFare;
        }

        totalFare += 800;
        int distance = totalDistance - 50;
        totalFare += calculateAdditionalFare(distance, 8);
        return totalFare;
    }

    private int calculateAdditionalFare(int distance, int unit) {
        return (int) ((Math.ceil((double) distance / unit)) * ADDITIONAL_FARE);
    }

    private static void isSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "출발역과 도착역이 같습니다.");
        }
    }

    private List<Section> allSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().get().stream())
                .collect(Collectors.toList());
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

    private int totalDistance(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDistance).sum();
    }

    private int totalDuration(GraphPath<Station, CustomWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().mapToInt(CustomWeightedEdge::getDuration).sum();
    }

    public void isConnected(Station sourceStation, Station targetStation) {
        shortestPath(sourceStation, targetStation, PathType.DISTANCE);
    }
}
