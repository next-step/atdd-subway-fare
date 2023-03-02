package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
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

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType pathType) {
        int age = 0;

        if (!loginMember.isGuest()) {
            age = memberService.findMember(loginMember.getId()).getAge();
        }

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);

        return PathResponse.of(path, age);
    }
}
