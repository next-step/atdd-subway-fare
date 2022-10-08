package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int surcharge;

    @JsonFormat(pattern = "HHmm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HHmm")
    private LocalTime endTime;

    private int interval;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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

    public int getSurcharge() {
        return surcharge;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getInterval() {
        return interval;
    }
}
