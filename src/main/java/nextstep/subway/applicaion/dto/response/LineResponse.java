package nextstep.subway.applicaion.dto.response;

import lombok.Getter;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final int additionalFare;
    private final List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), stations);
    }

    public LineResponse(Long id, String name, String color, int additionalFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.stations = stations;
    }
}
