package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationPair;
import nextstep.subway.domain.SubwayMap;
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

    public PathResponse findPath(PathSearchRequest request, LoginMember loginMember) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, request.getType());

        StationPair stationPair = new StationPair(upStation, downStation);
        if (loginMember != null) {
            Member member = memberService.findById(loginMember.getId());
            return PathResponse.of(subwayMap.findPathWithMember(stationPair, member));
        }

        return PathResponse.of(subwayMap.findPath(stationPair));
    }
}
