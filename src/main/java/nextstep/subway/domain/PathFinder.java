package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.common.exception.PathNotFoundException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path getShortestPath(List<Section> allSections, Station sourceStation, Station targetStation, PathType pathType, int age) {
        if (sourceStation.equals(targetStation)) {
            throw new PathNotFoundException(sourceStation.getId(), targetStation.getId());
        }

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPathStationGraph = dijkstraAlg.getPath(sourceStation, targetStation);

        if (shortestPathStationGraph == null) {
            throw new PathNotFoundException(sourceStation.getId(), targetStation.getId());
        }

        List<Station> pathStations = shortestPathStationGraph.getVertexList();

        int totalWeight = (int) shortestPathStationGraph.getWeight(); // 이것이 시간일 수도, 거리 일 수도

        return Path.createPath(pathStations, allSections, pathType, totalWeight, age);
    }
}
