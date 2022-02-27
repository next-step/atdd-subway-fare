package nextstep.subway.applicaion;

import java.util.List;
import nextstep.member.domain.LoginMember;
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
    private final SubwayMap subwayMap;

    public PathService(LineService lineService,
        StationService stationService, SubwayMap subwayMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMap = subwayMap;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType pathType, String time) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        subwayMap.createSubwayMapGraph(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType, time);

        path.farePolicySetting(loginMember.getAge());

        return PathResponse.of(path);
    }
}
