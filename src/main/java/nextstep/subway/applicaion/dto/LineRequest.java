package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalTime;

public class LineRequest {
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private int additionalFare;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public LineRequest() {
    }

    public LineRequest(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime, int additionalFare, Long upStationId, Long downStationId, int distance, int duration) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.additionalFare = additionalFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
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

    public Line toLineEntity() {
        return Line.builder()
                .name(this.name)
                .color(this.color)
                .additionalFare(this.additionalFare)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .intervalTime(this.intervalTime)
                .build();
    }
}
