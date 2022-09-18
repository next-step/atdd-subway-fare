package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Guest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public PathResponse findPath(PathType pathType, Long source, Long target, String userName) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = SubwayMap.create(lines, pathType);
        Member member = memberRepository.findByEmail(userName).orElse(new Guest());
        Path path = subwayMap.findPath(stationService.findById(source), stationService.findById(target), member.getAge());
        return PathResponse.of(path);
    }

}
