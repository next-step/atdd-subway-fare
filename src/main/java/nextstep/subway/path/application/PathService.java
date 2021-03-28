package nextstep.subway.path.application;

import nextstep.subway.path.domain.DiscountFareCalculator;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.AdditionalFareCalculator;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PathService {
    public static final int DEFAULT_FARE = 1250;

    private final GraphService graphService;
    private final StationService stationService;
    private final FareDiscountService fareDiscountService;

    public PathService(GraphService graphService, StationService stationService, FareDiscountService fareDiscountService) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.fareDiscountService = fareDiscountService;
    }

    public PathResponse findPath(
        Long source,
        Long target,
        PathType type,
        Optional<LoginMember> loginMember
    ) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        int fare = DEFAULT_FARE + fareDiscountService.calculateAdditionalFare(pathResult);

        return PathResponse.of(
            pathResult,
            fare - loginMember.map(it -> fareDiscountService.calculateDiscount(it, fare))
                                   .orElse(0)
        );
    }
}
