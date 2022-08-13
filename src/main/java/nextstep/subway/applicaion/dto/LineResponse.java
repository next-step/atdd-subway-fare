package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int surCharge;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getSurCharge(), stations);
    }

    public LineResponse(Long id, String name, String color, int surCharge,  List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.surCharge = surCharge;
        this.stations = stations;
    }

}

