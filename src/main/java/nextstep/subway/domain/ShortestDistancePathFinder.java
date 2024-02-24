package nextstep.subway.domain;

import nextstep.subway.application.dto.PathResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShortestDistancePathFinder implements PathFinder {
    @Override
    public boolean isType(final PathType pathType) {
        return PathType.DISTANCE == pathType;
    }

    @Override
    public PathResponse findPath(final List<Section> sections, final Station sourceStation, final Station targetStation) {
        if (sourceStation.isSame(targetStation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같습니다.");
        }

        WeightedMultigraph<Station, CustomWeightedEdge> graph = createGraph(sections);

        if (isNotContiansGraph(sourceStation, graph) || isNotContiansGraph(targetStation, graph)) {
            throw new IllegalArgumentException("그래프에 존재하지 않는 정점입니다.");
        }

        DijkstraShortestPath<Station, CustomWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, CustomWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        final int duration = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDuration)
                .sum();
        return new PathResponse(path.getVertexList(), (int) path.getWeight(), duration);
    }

    private static boolean isNotContiansGraph(final Station sourceStation, final WeightedMultigraph<Station, CustomWeightedEdge> graph) {
        return !graph.containsVertex(sourceStation);
    }

    private WeightedMultigraph<Station, CustomWeightedEdge> createGraph(final List<Section> sections) {
        WeightedMultigraph<Station, CustomWeightedEdge> graph = new WeightedMultigraph<>(CustomWeightedEdge.class);

        sections.forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    final CustomWeightedEdge customWeightedEdge = graph.addEdge(upStation, downStation);
                    customWeightedEdge.setDuration(section.getDuration());
                    graph.setEdgeWeight(customWeightedEdge, section.getDistance());
                });

        return graph;
    }
}


