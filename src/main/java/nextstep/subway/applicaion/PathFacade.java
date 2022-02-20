package nextstep.subway.applicaion;

import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Service;

import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.map.SectionEdge;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

@Service
public class PathFacade {
    private final LineService lineService;
    private final StationService stationService;
    private final FareCalculator fareCalculator;
    private final SubwayMap subwayMap;

    public PathFacade(FareCalculator fareCalculator, LineService lineService, StationService stationService, SubwayMap subwayMap) {
        this.fareCalculator = fareCalculator;
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMap = subwayMap;
    }

    public PathResponse findPathByDuration(PathRequest request, FareDiscountCondition fareDiscountCondition) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDuration());

        SourceTarget sourceTarget = findSourceTarget(request.getSource(), request.getTarget());
        Path path = subwayMap.findPath(graph, sourceTarget.source, sourceTarget.target);
        return createPathResponse(path, fareDiscountCondition);
    }

    public PathResponse findPathByDistance(PathRequest request, FareDiscountCondition fareDiscountCondition) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDistance());

        SourceTarget sourceTarget = findSourceTarget(request.getSource(), request.getTarget());
        Path path = subwayMap.findPath(graph, sourceTarget.source, sourceTarget.target);
        return createPathResponse(path, fareDiscountCondition);
    }

    public PathResponse findPathByArrivalTime(PathRequest request, FareDiscountCondition fareDiscountCondition) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDuration());

        SourceTarget sourceTarget = findSourceTarget(request.getSource(), request.getTarget());
        Path fastestPath = subwayMap.findPaths(graph, sourceTarget.source, sourceTarget.target)
                                    .fastestPathForArrivalTime(request.getTime());
        return createPathResponse(fastestPath, fareDiscountCondition);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(Function<SectionEdge, Double> getWeightStrategy) {
        return SubwayMapGraphFactory.newOneFieldWeightGraph(lineService.findLinesWithSections(), getWeightStrategy);
    }

    private SourceTarget findSourceTarget(long source, long target) {
        return new SourceTarget(stationService.findById(source), stationService.findById(target));
    }

    private PathResponse createPathResponse(Path path, FareDiscountCondition fareDiscountCondition) {
        return PathResponse.of(path, calculateFare(path, fareDiscountCondition));
    }

    private int calculateFare(Path path, FareDiscountCondition fareDiscountPolicy) {
        final int fare = fareCalculator.calculate(path);
        return fareDiscountPolicy.discount(fare);
    }

    private static class SourceTarget {
        private final Station source;
        private final Station target;

        public SourceTarget(Station source, Station target) {
            this.source = source;
            this.target = target;
        }
    }
}
