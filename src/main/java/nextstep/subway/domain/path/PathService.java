package nextstep.subway.domain.path;

import java.util.Map;

import nextstep.auth.domain.LoginMember;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.path.dto.PathResponse;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
    private MemberService memberService;
    private StationRepository stationRepository;
    private LineRepository lineRepository;
    private Map<String, PathFinder> finders;

    public PathService(MemberService memberService, StationRepository stationRepository, LineRepository lineRepository, Map<String, PathFinder> finders) {
        this.memberService = memberService;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.finders = finders;
    }

    public PathResponse findPath(Long source, Long tartget, String type, LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail()).orElse(null);

        List<Line> lines = lineRepository.findAll();
        List<Section> sections = lines.stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Station sourceStation = findByStationId(source);
        Station targetStation = findByStationId(tartget);

        PathFinder pathFinder = PathType.from(type).findPathFinder();
        var findResult = pathFinder.findPath(sourceStation, targetStation, sections).orElseThrow(() -> new IllegalArgumentException("요청한 경로를 찾을 수 없습니다."));
        List<Station> stations = findResult.getVertexList();

        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        Long distance = pathFinder.getDistance();
        Long transitTime = pathFinder.getTransitTime();
        Fare fare = new Fare();
        fare.calculateDistanceBasedFare(distance, lines, member);

        return new PathResponse(stationResponses, distance, transitTime, fare.getFare());
    }

    public Station findByStationId(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("조회할 역이 존재하지 않습니다."));
    }
}
