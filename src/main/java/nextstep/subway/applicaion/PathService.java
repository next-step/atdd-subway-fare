package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private MemberService memberService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType, Long memberId) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        Integer age = memberService.findMember(memberId).getAge();

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);

        return PathResponse.of(path);
    }
}
