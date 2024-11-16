package nextstep.subway.domain.path;

import java.util.Map;

import nextstep.auth.domain.Account;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FarePolicyContext;
import nextstep.subway.domain.fare.FarePolicyManager;
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
    private FarePolicyManager farePolicyManager;

    public PathService(MemberService memberService, StationRepository stationRepository, LineRepository lineRepository, Map<String, PathFinder> finders, FarePolicyManager farePolicyManager) {
        this.memberService = memberService;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.finders = finders;
        this.farePolicyManager = farePolicyManager;
    }

    public PathResponse findPath(Long source, Long tartget, String type, Account account) {
        Member member = memberService.findMemberByEmail(account.getEmail()).orElse(null);

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

        // 기본 요금을 가진 fare객체 생성
        Fare fare = new Fare();
        FarePolicyContext context = new FarePolicyContext(distance, lines, member);
        Fare resultFare = farePolicyManager.calculateFare(context, fare);

        return new PathResponse(stationResponses, distance, transitTime, resultFare.getFare());
    }

    public Station findByStationId(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("조회할 역이 존재하지 않습니다."));
    }
}
