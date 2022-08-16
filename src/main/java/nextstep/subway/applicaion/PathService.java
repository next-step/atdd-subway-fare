package nextstep.subway.applicaion;

import lombok.AllArgsConstructor;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.service.SubwayMap;
import nextstep.subway.domain.service.chain.FareChainCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FareChainCalculator fareChainCalculator;


    public PathResponse findPath(Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type);

        return PathResponse.of(path, fareChainCalculator.operate(path.extractDistance()));
    }
}
