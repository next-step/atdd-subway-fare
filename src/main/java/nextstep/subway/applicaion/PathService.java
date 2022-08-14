package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.subway.builder.PathResponseBuilder;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.constant.SearchType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final MemberService memberService;
    private final LineService lineService;
    private final StationService stationService;
    private final PathResponseBuilder pathResponseBuilder;

    public PathService(MemberService memberService, LineService lineService, StationService stationService, PathResponseBuilder pathResponseBuilder) {
        this.memberService = memberService;
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathResponseBuilder = pathResponseBuilder;
    }

    public PathResponse findPath(String email, Long source, Long target, SearchType searchType) {
        int age = memberService.findMember(email).getAge();
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, searchType);

        return pathResponseBuilder.build(path, age);
    }
}
