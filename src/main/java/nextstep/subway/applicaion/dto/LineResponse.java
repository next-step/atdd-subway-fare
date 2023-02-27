package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private BigDecimal fare;
    private List<StationResponse> stations;

    public LineResponse(final Long id, final String name, final String color, final BigDecimal fare, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.fare = fare;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        final List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare().getFare(), stations);
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

    public BigDecimal getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}

