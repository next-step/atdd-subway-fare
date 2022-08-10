package nextstep.subway.applicaion.builder;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

@Component
public class PathResponseBuilder {

    private static final int MINIMUM_DISTANCE = 10;
    private static final int MINIMUM_FARE = 1250;

    private static final IntUnaryOperator BETWEEN_10_AND_50 = d -> (int) Math.ceil(d / (double) 5) * 100;
    private static final IntUnaryOperator OVER_50 = d -> (int) Math.ceil(d / (double) 8) * 100;

    public PathResponse build(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = distance == 0 ? 0 : MINIMUM_FARE + calculateOverFare(distance);

        return new PathResponse(stations, distance, duration, fare);
    }

    private int calculateOverFare(int distance) {
        int over50 = Math.max(distance - 50, 0);
        int between10and50 = Math.max(distance - over50 - MINIMUM_DISTANCE, 0);

        return BETWEEN_10_AND_50.applyAsInt(between10and50) + OVER_50.applyAsInt(over50);
    }
}
