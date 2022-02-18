package nextstep.subway.applicaion;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.map.SectionEdge;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Service;

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

    public PathResponse findPathByDuration(PathRequest request, FareDiscountCondition fareDiscountPolicy) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDuration());
        return findPathByOneField(request.getSource(), request.getTarget(), fareDiscountPolicy, graph);
    }

    public PathResponse findPathByDistance(PathRequest request, FareDiscountCondition fareDiscountPolicy) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDistance());
        return findPathByOneField(request.getSource(), request.getTarget(), fareDiscountPolicy, graph);
    }

    private PathResponse findPathByOneField(
        long source, long target, FareDiscountCondition fareDiscountPolicy,  SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        SourceTarget sourceTarget = findSourceTarget(source, target);
        Path path = subwayMap.findPath(graph, sourceTarget.source, sourceTarget.target);
        return PathResponse.of(path, calculateFare(path, fareDiscountPolicy));
    }

    public PathResponse findPathByArrivalTime(PathRequest request, FareDiscountCondition fareDiscountPolicy) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(edge -> (double) edge.getSection().getDuration());

        SourceTarget sourceTarget = findSourceTarget(request.getSource(), request.getTarget());
        Path fastestPath = subwayMap.findPaths(graph, sourceTarget.source, sourceTarget.target)
                                    .fastestPath(request.getTime());
        return PathResponse.of(fastestPath, calculateFare(fastestPath, fareDiscountPolicy));
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(Function<SectionEdge, Double> getWeightStrategy) {
        return SubwayMapGraphFactory.newOneFieldWeightGraph(lineService.findLines(), getWeightStrategy);
    }

    private SourceTarget findSourceTarget(long source, long target) {
        return new SourceTarget(stationService.findById(source), stationService.findById(target));
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
