package nextstep.subway.maps.line.dto;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private List<LineStationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer extraFare;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime, List<LineStationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate, Integer extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.extraFare = extraFare;
    }

    public static LineResponse of(Line line, List<LineStationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), stations, line.getCreatedDate(), line.getModifiedDate(), line.getExtraFare().amount());
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), Lists.newArrayList(), line.getCreatedDate(), line.getModifiedDate(), line.getExtraFare().amount());
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public List<LineStationResponse> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
