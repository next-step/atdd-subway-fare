package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;
    private MemberFarePolicy farePolicy;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.farePolicy = new BaseMemberFarePolicy(new BaseFarePolicy());
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Path path = findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType(), pathRequest.getDepartureDate());
        Fare fare = new Fare(path);
        int fareAmount = fare.calculate(farePolicy);
        return PathResponse.of(path, fareAmount);
    }

    public PathResponse findPath(Long memberId, PathRequest pathRequest) {
        Member member = memberService.findById(memberId);
        Path path = findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType(), pathRequest.getDepartureDate());
        Fare fare = new Fare(member, path);
        int fareAmount = fare.calculate(farePolicy);
        return PathResponse.of(path, fareAmount);
    }

    public Path findPath(Long source, Long target, PathType pathType, LocalDateTime departureDate) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        SubwayMap subwayMap = createSubwayMap(pathType);
        if (Objects.nonNull(departureDate)) {
            Paths paths = subwayMap.findPaths(upStation, downStation);
            return paths.getMiniumArrivalPath(departureDate);
        }
        return subwayMap.findPath(upStation, downStation);
    }


    private SubwayMap createSubwayMap(PathType pathType) {
        List<Line> lines = lineService.findLines();
        return new SubwayMap(lines, pathType);
    }
}
