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
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public void checkPath(Long source, Long target) {
        findPath(source, target, PathType.DISTANCE, MemberResponse.noLoginResponse());
    }

    public PathResponse findPath(Long source, Long target, PathType type, UserPrincipal userPrincipal) {
        MemberResponse member = userPrincipal != null ? memberService.findMemberByEmail(userPrincipal.getUsername()) : MemberResponse.noLoginResponse();
        return findPath(source, target, type, member);
    }

    private PathResponse findPath(Long source, Long target, PathType type, MemberResponse member) {

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        Path resultPath = new SubwayMap(lines, type).findPath(upStation, downStation);

        if (type == PathType.DISTANCE) {
            int fare = new FareCalculator(resultPath.extractDistance(), resultPath.getAdditionalFareByLine(), member.getAge()).fare();
            return PathResponse.of(resultPath, fare);
        }

        Path pathByDistance = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
        int fare =  new FareCalculator(pathByDistance.extractDistance(), pathByDistance.getAdditionalFareByLine(), member.getAge()).fare();

        return PathResponse.of(resultPath, fare);
    }
}
