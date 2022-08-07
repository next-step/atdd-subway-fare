package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(PathRequest pathRequest, int userAge) {
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        Lines lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines.getLines(), pathRequest.getPathType());
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path, userAge, lines.getHigherSurCharge());
    }
}
