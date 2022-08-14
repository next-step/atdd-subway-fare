package nextstep.subway.builder;

import nextstep.subway.applicaion.FareCalculator;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathResponseBuilder {

    private final FareCalculator fareCalculator;

    public PathResponseBuilder(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public PathResponse build(Path path, int age) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = fareCalculator.calculateOverFare(distance, age);

        return new PathResponse(stations, distance, duration, fare);
    }
}
