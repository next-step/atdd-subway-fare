package nextstep.path.applicaion;

import nextstep.auth.userdetails.User;
import nextstep.line.application.LineService;
import nextstep.line.domain.Line;
import nextstep.member.domain.Member;
import nextstep.path.applicaion.dto.PathRequest;
import nextstep.path.applicaion.dto.PathResponse;
import nextstep.path.domain.Path;
import nextstep.path.domain.SubwayMap;
import nextstep.path.domain.fare.FareCalculator;
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
    private final PathMemberService memberService;

    public PathService(LineService lineService, StationService stationService, PathMemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(User user, PathRequest request) {
        Member member = memberService.findMemberForDiscountByUser(user);
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation.getId(), downStation.getId(), request.getType());
        List<Long> pathStationIds = path.getStations();

        int distance = subwayMap.shortestDistance(request.getSource(), request.getTarget());
        int fare = new FareCalculator(member.getAge(), lines, path.getSections(), distance).calculate();

        return new PathResponse(createStationResponses(pathStationIds), path, fare);
    }

    private List<StationResponse> createStationResponses(List<Long> stationIds) {
        return stationService.findAllStationsById(stationIds)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
