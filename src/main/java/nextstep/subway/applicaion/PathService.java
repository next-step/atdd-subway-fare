package nextstep.subway.applicaion;

import nextstep.member.domain.GuestMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.util.fare.FareCalculator;
import nextstep.subway.util.pathfinder.FastestArrivalTimePathFinder;
import nextstep.subway.util.pathfinder.PathType;
import nextstep.subway.util.pathfinder.ShortestDistancePathFinder;
import nextstep.subway.util.pathfinder.ShortestDurationPathFinder;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.subway.util.pathfinder.PathType.*;

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

    public PathResponse findPath(String email, PathRequest request) {
        Member member = findMemberByEmail(email);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();

        Path shortestPath = ShortestDistancePathFinder.find(lines, source, target);
        int fare = FareCalculator.calculate(shortestPath, member.getAge());

        if (request.getType() == ARRIVAL_TIME) {
            return FastestArrivalTimePathFinder.find(lines, request.getTime(), source, target, fare);
        }

        if (request.getType() == DISTANCE) {
            return PathResponse.of(shortestPath, fare);
        }

        if (request.getType() == DURATION) {
            Path path = ShortestDurationPathFinder.find(lines, source, target);
            return PathResponse.of(path, fare);
        }

        throw new IllegalArgumentException("지원하지 않는 경로타입입니다.");
    }

    private Member findMemberByEmail(String email) {
        if (email == null) {
            return GuestMember.create();
        }
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }
}
