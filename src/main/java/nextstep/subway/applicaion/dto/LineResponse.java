package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;
    private LocalTime firstTime;
    private LocalTime lastTime;
    private int interval;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, Integer additionalFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.stations = stations;
    }

    public LineResponse(Long id, String name, String color, Integer additionalFare, LocalTime firstTime, LocalTime lastTime, int interval, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.interval = interval;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(),
                line.getFirstTime(), line.getLastTime(), line.getIntervalMinute(), stations);
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

    public Integer getAdditionalFare() {
        return additionalFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getLastTime() {
        return lastTime;
    }

    public int getInterval() {
        return interval;
    }
}

