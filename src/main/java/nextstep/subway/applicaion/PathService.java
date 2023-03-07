package nextstep.subway.applicaion;

import nextstep.member.domain.AuthenticatedUser;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SearchType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
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

    public PathResponse findPath(AuthenticatedUser user, Long source, Long target, SearchType searchType) {
        Path path = getPath(source, target, searchType);

        int totalFare = fareService.getTotalFare(user, path);

        return PathResponse.of(path, totalFare);
    }

    private Path getPath(Long source, Long target, SearchType searchType) {
        Station departureStation = stationService.findById(source);
        Station destinationStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, searchType);
        Path path = subwayMap.findPath(departureStation, destinationStation);
        return path;
    }
}
