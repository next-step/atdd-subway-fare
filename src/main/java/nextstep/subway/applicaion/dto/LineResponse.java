package nextstep.subway.applicaion.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    public LineResponse(Line line) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        extraFare = line.getExtraFare();
        stations = line.getStations().stream().map(StationResponse::new).collect(Collectors.toList());
    }

    public LineResponse(Long id, String name, String color, int extraFare) {
       this.id = id;
       this.name = name;
       this.color = color;
       this.extraFare = extraFare;
       stations = new ArrayList<>();
    }

    public LineResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}

