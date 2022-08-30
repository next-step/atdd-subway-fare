package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_TIME;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStartTime(),
                line.getEndTime(),
                line.getIntervalTime(),
                stations
        );
    }

    public LineResponse(Long id, String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
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

    public String getStartTime() {
        if (this.startTime == null) {
            return null;
        }
        return startTime.format(ISO_TIME);
    }

    public String getEndTime() {
        if (this.startTime == null) {
            return null;
        }
        return endTime.format(ISO_TIME);
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}

