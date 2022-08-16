package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int surcharge;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getSurcharge());
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, int surcharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.surcharge = surcharge;
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

    public int getSurcharge() {
        return surcharge;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}

