package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathResponse findPath(PathRequest pathRequest) {
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineService.findLines();
        PathType type = PathType.of(pathRequest.getType());
        
        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
