package nextstep.subway.applicaion;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.PathResponse;
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
    private final MemberService memberService;

    public PathService(
            final LineService lineService,
            final StationService stationService,
            final MemberService memberService
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target, String pathTypeName) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, PathType.findBy(pathTypeName));

        return PathResponse.of(path, path.calculateFare());
    }

    public PathResponse findPath(Long loginMember, Long source, Long target, String pathTypeName) {
        Integer age = memberService.findMember(loginMember).getAge();

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, PathType.findBy(pathTypeName), age);

        return PathResponse.of(path, path.calculateFare());
    }
}
