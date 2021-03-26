package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineFare;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final GraphService graphService;
    private final StationService stationService;
    private final LineFareService lineFareService;

    public PathService(GraphService graphService, StationService stationService, LineFareService lineFareService) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.lineFareService = lineFareService;
    }

    public PathResponse findPath(
        Long source,
        Long target,
        PathType type,
        LoginMember loginMember
    ) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        FareCalculator fareCalculator = new DistanceProportionFareCalculator(
            LineFare.ofMember(loginMember),
            lineFareService.getAdditionalFareOf(pathResult)
        );

        return PathResponse.of(pathResult, fareCalculator);
    }
}
