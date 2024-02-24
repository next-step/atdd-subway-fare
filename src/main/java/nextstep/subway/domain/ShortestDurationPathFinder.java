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
public class ShortestDurationPathFinder implements PathFinder {
    @Override
    public boolean isType(final PathType pathType) {
        return PathType.DURATION == pathType;
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
        final double distance = path.getEdgeList().stream()
                .mapToDouble(CustomWeightedEdge::getDistance)
                .sum();
        return new PathResponse(path.getVertexList(), (int) distance, (int) path.getWeight());
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
            customWeightedEdge.setDistance(section.getDistance());
            graph.setEdgeWeight(customWeightedEdge, section.getDuration());
        });

        return graph;
    }
}
