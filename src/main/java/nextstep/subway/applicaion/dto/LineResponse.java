package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int distance;
    private int duration;
    private int extraCharge;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations,
                line.getCreatedDate(), line.getModifiedDate(), line.totalDistance(), line.totalDuration(), line.getExtraCharge());
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations,
                        LocalDateTime createdDate, LocalDateTime modifiedDate, int distance, int duration, int extraCharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.distance = distance;
        this.duration = duration;
        this.extraCharge = extraCharge;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getExtraCharge() {
        return extraCharge;
    }
}

