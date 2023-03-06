package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathResponse findPath(Long source, Long target, PathType pathType, Long memberId) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        Integer age = memberService.findMember(memberId).getAge();

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);

        return PathResponse.of(path, age);
    }
}
