package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FeePolicy feePolicy;

    public PathService(LineService lineService, StationService stationService, FeePolicy feePolicy) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.feePolicy = feePolicy;
    }

    public PathResponse findPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }

    public PathResponse findPathByMinimumTime(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPathByDuration(upStation, downStation);
        int fee = feePolicy.totalFee(path.extractDistance(), subwayMap.totalAdditionalFee());

        return PathResponse.of(path, fee);
    }

    public PathResponse findPathByMinimumFee(int age, Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);
        int fee = feePolicy.totalFee(path.extractDistance(), subwayMap.totalAdditionalFee(), age);

        return PathResponse.of(path, fee);
    }

}
