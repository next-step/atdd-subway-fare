package nextstep.subway.applicaion;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        findPath(source, target, PathType.DISTANCE, Optional.empty());
    }

    public PathResponse findPath(Long source, Long target, PathType type, UserPrincipal userPrincipal) {

        if (userPrincipal.isLoginUser()) {
            int age = memberService.findMemberByEmail(userPrincipal.getUsername()).getAge();
            return findPath(source, target, type, Optional.of(age));
        }

        return findPath(source, target, type, Optional.empty());
    }

    private PathResponse findPath(Long source, Long target, PathType type, Optional<Integer> age) {

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        Path resultPath = new SubwayMap(lines, type).findPath(upStation, downStation);

        if (type == PathType.DISTANCE) {
            int fare = new FareCalculator(resultPath.extractDistance(), resultPath.getAdditionalFareByLine(), age).fare();
            return PathResponse.of(resultPath, fare);
        }

        Path pathByDistance = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
        int fare =  new FareCalculator(pathByDistance.extractDistance(), pathByDistance.getAdditionalFareByLine(), age).fare();

        return PathResponse.of(resultPath, fare);
    }
}
