package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.enumeration.FareDistanceType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;

    public PathService(GraphService graphService, StationService stationService) {
        this.graphService = graphService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType type, LoginMember loginMember) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        Fare fare = calculate(pathResult, loginMember);
        return PathResponse.of(pathResult, fare.getFare());
    }


    public Fare calculate(PathResult pathResult, LoginMember loginMember) {
        Fare fare = new Fare(pathResult.getTotalDistance(), pathResult.);

        if (loginMember != null) {
            fare.setAgePolicy(loginMember);
        }
    }

    public Fare calculate(PathResult pathResult, LoginMember loginMember) {
        Fare fare = new Fare(pathResult.getTotalDistance(), pathResult.getMaxAdditionalFare());

        if (loginMember != null) {
            fare.setAgePolicy(loginMember);
        }
        return fare;
    }
}
