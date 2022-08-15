package nextstep.path.applicaion;

import nextstep.auth.userdetails.User;
import nextstep.line.application.LineService;
import nextstep.line.domain.Line;
import nextstep.path.applicaion.dto.PathRequest;
import nextstep.path.applicaion.dto.PathResponse;
import nextstep.path.domain.Path;
import nextstep.path.domain.SubwayMap;
import nextstep.path.domain.fare.DistanceFarePolicy;
import nextstep.path.domain.fare.FareCalculator;
import nextstep.path.domain.fare.LineExtraFarePolicy;
import nextstep.station.application.StationService;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(User user, PathRequest request) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation.getId(), downStation.getId(), request.getType());
        List<Long> pathStationIds = path.getStations();

        FareCalculator fareCalculator = new FareCalculator(List.of(
                new DistanceFarePolicy(subwayMap.shortestDistance(request.getSource(), request.getTarget())),
                new LineExtraFarePolicy(lines, path.getSections())
        ));
        return new PathResponse(createStationResponses(pathStationIds), path, fareCalculator.calculate());
    }

    private List<StationResponse> createStationResponses(List<Long> stationIds) {
        return stationService.findAllStationsById(stationIds)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
