package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType, String email) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, pathType);
        Path shortestDistancePath;

        if (pathType == PathType.DISTANCE) {
            shortestDistancePath = new Path(path.getSections());
        } else {
            shortestDistancePath = subwayMap.findPath(upStation, downStation, PathType.DISTANCE);
        }

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> new Member(email, "", 0));

        return PathResponse.of(path, shortestDistancePath, member);
    }
}
