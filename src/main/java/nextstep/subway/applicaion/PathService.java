package nextstep.subway.applicaion;

import nextstep.member.domain.GuestMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.util.fare.FareCalculator;
import nextstep.subway.util.subwaymap.SubwayMap;
import nextstep.subway.util.subwaymap.SubwayMapFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final SubwayMapFactory subwayMapFactory;
    private final MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService,
                       SubwayMapFactory subwayMapFactory, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMapFactory = subwayMapFactory;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(String email, Long source, Long target, String type) {
        Member member = findMemberByEmail(email);
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = subwayMapFactory.subwayMap(type);
        Path path = subwayMap.path(lines, upStation, downStation);

        if (subwayMap.isDefaultPathShortest()) {
            return PathResponse.of(path, FareCalculator.calculate(path, member.getAge()));
        }

        Path shortestPath = subwayMap.shortestPath(lines, upStation, downStation);
        return PathResponse.of(path, FareCalculator.calculate(shortestPath, member.getAge()));
    }

    private Member findMemberByEmail(String email) {
        if (email == null) {
            return GuestMember.create();
        }
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }
}
