package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.path.fare.FareCalculator;
import nextstep.station.StationRepository;
import nextstep.station.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PathInfo {
    private List<String> stationIds;
    private int distance;
    private int duration;

    public PathResponse toResponse(StationRepository stationRepository) {
        List<StationResponse> stations = stationIds.stream()
                .map(Long::parseLong)
                .map(stationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(StationResponse::from)
                .collect(Collectors.toList());

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculate();

        return new PathResponse(stations, distance, duration, fare);
    }
}
