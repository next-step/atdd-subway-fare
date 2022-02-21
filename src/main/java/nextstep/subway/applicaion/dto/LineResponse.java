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
    private String startTime;
    private String endTime;
    private int intervalTime;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations
            , line.getCreatedDate(), line.getModifiedDate(), line.getStartTime(), line.getEndTime(), line.getIntervalTime());
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations
        , LocalDateTime createdDate, LocalDateTime modifiedDate, String startTime, String endTime, int intervalTime) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }
}

