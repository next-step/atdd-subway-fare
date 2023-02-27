package nextstep.subway.applicaion;

import nextstep.member.domain.User;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.fare.FareService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    private final FareService fareService;

    public PathService(LineService lineService, StationService stationService, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareService = fareService;
    }

    public PathResponse findPath(Long source, Long target, PathLookUpType type, User user) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type);
        int totalFare = calculateTotalFare(path, user);

        return PathResponse.from(path, totalFare);
    }

    private int calculateTotalFare(Path path, User user) {
        final int fare = fareService.calculateFare(path.extractDistance());
        final int totalFare = fareService.calculateExtraFare(fare, path.getLines());
        return user.applyFarePolicy(totalFare);
    }
}
