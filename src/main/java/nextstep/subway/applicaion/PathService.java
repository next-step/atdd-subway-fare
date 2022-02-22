package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
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

    public PathResponse findPath(PathSearchRequest pathSearchRequest, Integer age) {
        Station upStation = stationService.findById(pathSearchRequest.getSource());
        Station downStation = stationService.findById(pathSearchRequest.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathSearchRequest.getMethod());
        int fee = feePolicy.totalFee(path.extractDistance(), subwayMap.totalAdditionalFee(), age);

        return PathResponse.createResponse(path, fee);
    }
}
