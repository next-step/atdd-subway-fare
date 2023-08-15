package nextstep.subway.applicaion;

import nextstep.auth.principal.UnknownUserPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
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

    public PathResponse findPath(UserPrincipal userPrincipal, Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type);
        if (!(userPrincipal instanceof UnknownUserPrincipal)) {
            Member member = memberService.findMemberByUserPrincipal(userPrincipal);
            path.discountFare(member);
        }
        return PathResponse.of(path);
    }
}
