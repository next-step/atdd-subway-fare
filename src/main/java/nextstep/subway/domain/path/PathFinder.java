package nextstep.subway.domain.path;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.CustomWeightedEdge;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.fee.CalculateHandler;
import nextstep.subway.domain.path.fee.Distance;
import nextstep.subway.domain.path.fee.FeeInfo;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class PathFinder {

    private CalculateHandler calculateHandler;

    public PathFinder(final CalculateHandler calculateHandler) {
        this.calculateHandler = calculateHandler;
    }

    public PathResponse findPath(final List<Line> lines, final Station sourceStation, final Station targetStation) {
        checkSameStation(sourceStation, targetStation);

        WeightedMultigraph<Station, CustomWeightedEdge> graph = createGraph(lines);

        checkStationContainsGraph(sourceStation, targetStation, graph);

        DijkstraShortestPath<Station, CustomWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, CustomWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return createPathResponse(path);
    }

    protected abstract WeightedMultigraph<Station, CustomWeightedEdge> createGraph(final List<Line> lines);

    protected abstract PathResponse createPathResponse(final GraphPath<Station, CustomWeightedEdge> path);

    private void checkSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.isSame(targetStation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같습니다.");
        }
    }

    private void checkStationContainsGraph(final Station sourceStation, final Station targetStation, final WeightedMultigraph<Station, CustomWeightedEdge> graph) {
        if (isNotContainsGraph(sourceStation, graph)) {
            throw new IllegalArgumentException("구간에 포함되지 않은 지하철역: " + sourceStation.getName());
        }

        if (isNotContainsGraph(targetStation, graph)) {
            throw new IllegalArgumentException("구간에 포함되지 않은 지하철역: " + targetStation.getName());
        }
    }

    private boolean isNotContainsGraph(final Station sourceStation, final WeightedMultigraph<Station, CustomWeightedEdge> graph) {
        return !graph.containsVertex(sourceStation);
    }

    protected int calculateFare(int distance, List<Integer> additionalFees) {
        final FeeInfo feeInfo = FeeInfo.of(new Distance(distance), additionalFees);
        calculateHandler.handle(feeInfo);
        return calculateHandler.fare().value();
    }
}
