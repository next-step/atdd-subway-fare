package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathMinimumDistanceResponse;
import nextstep.subway.applicaion.dto.PathMinimumDurationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathMinimumDistanceResponse findPathOfMinimumDistance(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPathOfMinimumDistance(upStation, downStation);

        return PathMinimumDistanceResponse.of(path);
    }

    public PathMinimumDurationResponse findPathOfMinimumDuration(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPathOfMinimumDuration(upStation, downStation);

        return PathMinimumDurationResponse.of(path);
    }
}
