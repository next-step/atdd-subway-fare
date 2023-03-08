package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathResponse findPath(Long source, Long target, PathType pathType, Optional<Long> memberId) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);

        Optional<Integer> age = memberId.map(id -> memberService.findMember(id).getAge());
        int fare = path.calculateFare(age);

        return PathResponse.of(path, fare);
    }
}
