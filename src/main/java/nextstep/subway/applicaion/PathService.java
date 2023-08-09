package nextstep.subway.applicaion;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        return findPath(source, target, type, MemberResponse.noLoginResponse());
    }

    public PathResponse findPath(Long source, Long target, PathType type, UserPrincipal userPrincipal) {
        MemberResponse member = userPrincipal != null ? memberService.findMemberByEmail(userPrincipal.getUsername()) : MemberResponse.noLoginResponse();
        return findPath(source, target, type, member);
    }

    private PathResponse findPath(Long source, Long target, PathType type, MemberResponse member) {

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);

        int fare = new FareCalculator(path.extractDistance(), path.getAdditionalFareByLine(), member.getAge()).fare();

        if (type != PathType.DISTANCE) {
            Path distancePath = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
            fare = findFareByDistance(distancePath, member.getAge());
        }

        return PathResponse.of(path, fare);
    }

    private static int findFareByDistance(Path distancePath, Integer age) {
        return new FareCalculator(distancePath.extractDistance(), distancePath.getAdditionalFareByLine(), age).fare();
    }
}
