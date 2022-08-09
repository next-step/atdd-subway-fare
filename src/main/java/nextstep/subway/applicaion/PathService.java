package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_FARE = 800;
    private static final int PER_FARE = 100;
    private static final int FIRST_REFERENCE_DISTANCE = 10;
    private static final int SECOND_REFERENCE_DISTANCE = 50;
    private static final int FIRST_REFERENCE = 5;
    private static final int SECOND_REFERENCE = 8;
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        return getPathResponse(path);
    }

    public PathResponse getPathResponse(Path path) {
        List<StationResponse> stations = path.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = DEFAULT_FARE;
        if (FIRST_REFERENCE_DISTANCE < distance) {
            fare += calculateOverFare(distance);
        }
        return new PathResponse(stations, distance, duration, fare);
    }

    private static int calculateOverFare(int distance) {
        if (distance <= SECOND_REFERENCE_DISTANCE) {
            BigDecimal firstOverFare = getScale(distance - FIRST_REFERENCE_DISTANCE, FIRST_REFERENCE);
            return firstOverFare.intValue() * PER_FARE;
        }

        BigDecimal secondOverFare = getScale(distance - SECOND_REFERENCE_DISTANCE, SECOND_REFERENCE);
        return ADDITIONAL_FARE + secondOverFare.intValue() * PER_FARE;
    }

    private static BigDecimal getScale(int distance, int x) {
        return BigDecimal.valueOf((distance - ONE) / x + ONE).setScale(ZERO, RoundingMode.CEILING);
    }
}
