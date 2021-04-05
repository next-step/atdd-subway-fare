package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PathService {
    private final GraphService graphService;
    private final StationService stationService;
    private final FareCalculator fareCalculator;
    private final int DEFAULT_AGE = 30;

    public PathService(GraphService graphService, StationService stationService, FareCalculator fareCalculator) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target){
        return findPath(loginMember, source,target, PathType.DURATION);
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        int totalFare = fareCalculator.getTotalFare(pathResult.getSections(), loginMember.getAge());
        return PathResponse.of(pathResult, totalFare);
    }
}
