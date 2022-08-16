package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int additionalCharge;

    public LineResponse(long id, String name, String color, int additionalCharge, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalCharge = additionalCharge;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalCharge(), stations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getAdditionalCharge() {
        return additionalCharge;
    }
}

