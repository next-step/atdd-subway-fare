package nextstep.subway.applicaion;

import java.util.List;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;
    private final FareCalculator fareCalculator;

    public PathService(LineService lineService,
        StationService stationService, MemberRepository memberRepository,
        FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        Member member = memberRepository.findById(loginMember.getId())
            .orElse(new Member());

        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);
        path.setFareCalculator(fareCalculator);
        path.calculateFare(member.getAge());

        return PathResponse.of(path);
    }
}
