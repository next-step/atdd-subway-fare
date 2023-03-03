package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FarePolicyManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;
    private final FarePolicyManager fareManager;

    public PathService(LineService lineService, StationService stationService, MemberService memberService, FarePolicyManager fareManager) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.fareManager = fareManager;
    }

    public PathResponse findPath(PathSearchRequest request, LoginMember loginMember) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, request.getType());

        Member member = memberService.findByLoginMember(loginMember);
        Path path = subwayMap.findPath(upStation, downStation);

        int cost = fareManager.calculate(path, member);
        return PathResponse.of(path, cost);
    }
}
