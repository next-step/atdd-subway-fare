package nextstep.subway.applicaion.dto;

import java.time.LocalTime;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int additionalFare;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime intervalTime;

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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getIntervalTime() {
        return intervalTime;
    }
}
