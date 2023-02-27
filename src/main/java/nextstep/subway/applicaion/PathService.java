package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
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

    public PathResponse findPath(Long source, Long target, PathRequestType type, LoginMember loginMember) {
        Member member = memberService.findMember(loginMember.getId());
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type, member);

        return PathResponse.of(path);
    }

}
