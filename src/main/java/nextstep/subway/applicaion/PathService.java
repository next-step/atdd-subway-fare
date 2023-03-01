package nextstep.subway.applicaion;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(PathSearchRequest request, LoginMember loginMember) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, request.getType());

        if (loginMember != null) {
            Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
            return PathResponse.of(subwayMap.findPath(upStation, downStation, member));
        }

        return PathResponse.of(subwayMap.findPath(upStation, downStation));
    }
}
