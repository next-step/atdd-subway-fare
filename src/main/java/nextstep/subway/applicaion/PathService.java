package nextstep.subway.applicaion;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFindType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;

    public PathService(
        LineService lineService,
        StationService stationService,
        MemberService memberService
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(
        UserPrincipal userPrincipal,
        Long source,
        Long target,
        PathFindType pathFindType
    ) {
        MemberResponse memberResponse = memberService.findMemberByEmail(userPrincipal.getUsername());
        Path path = pathTemplate(source, target, pathFindType);

        return PathResponse.of(path, memberResponse.getAge());
    }

    public PathResponse findPath(Long source, Long target, PathFindType pathFindType) {
        Path path = pathTemplate(source, target, pathFindType);

        return PathResponse.of(path);
    }

    private Path pathTemplate(Long source, Long target, PathFindType pathFindType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathFindType);

        return path;
    }

    public boolean isPathUnlinked(Long source, Long target) {
        try {
            findPath(source, target, PathFindType.DURATION);
        } catch (IllegalArgumentException e) {
            return true;
        }

        return false;
    }
}
