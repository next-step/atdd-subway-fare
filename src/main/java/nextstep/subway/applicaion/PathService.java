package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FarePolicy;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FarePolicy farePolicy;

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType);

        path.addFare(farePolicy.getFare(path, loginMember));
        return PathResponse.of(path);
    }
}
