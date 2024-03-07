package nextstep.subway.path;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private final PathFinderStrategy pathFinderStrategy;

    public PathFinder(PathFinderStrategy pathFinderStrategy) {
        this.pathFinderStrategy = pathFinderStrategy;
    }

    public PathResponse getPath(List<Sections> sectionsList, Station sourceStation, Station targetStation) {
        try {
            WeightedMultigraph<Station, PathWeightEdge> graph = initGraph(sectionsList);

            GraphPath<Station, PathWeightEdge> path = getShortestPath(graph, sourceStation, targetStation);

            List<Station> stations = path.getVertexList();
            int distance = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDistance).sum();
            int duration = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDuration).sum();

            return new PathResponse(
                    stations
                            .stream()
                            .map(StationResponse::ofEntity)
                            .collect(Collectors.toList()),
                    distance,
                    duration
            );

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new PathException("연결되어있지 않은 출발역과 도착역의 경로는 조회할 수 없습니다.");
        }
    }

    private WeightedMultigraph<Station, PathWeightEdge> initGraph(List<Sections> sectionsList) {
        WeightedMultigraph<Station, PathWeightEdge> graph = new WeightedMultigraph<>(PathWeightEdge.class);

        for (Sections sections : sectionsList) {
            for (Section section : sections.getSections()) {
                addStations(graph, section);
                pathFinderStrategy.addEdge(graph, section);
            }
        }

        return graph;
    }

    private void addStations(WeightedMultigraph<Station, PathWeightEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private GraphPath<Station, PathWeightEdge> getShortestPath(
            WeightedMultigraph<Station, PathWeightEdge> graph,
            Station sourceStation,
            Station targetStation
    ) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }
}
