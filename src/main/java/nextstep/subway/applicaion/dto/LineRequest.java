package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Objects;

public class LineRequest {
    private String name;
    private String color;
    private Integer additionalFare;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime firstTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime lastTime;
    private int intervalMinute;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public Line toEntity() {
        if (Objects.isNull(firstTime)) {
            firstTime = LocalTime.of(0, 0);
        }
        if (Objects.isNull(lastTime)) {
            lastTime = LocalTime.of(0, 0);
        }
        return new Line(name, color, additionalFare, firstTime, lastTime, intervalMinute);
    }

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

    public Integer getAdditionalFare() {
        return additionalFare;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getLastTime() {
        return lastTime;
    }

    public int getIntervalMinute() {
        return intervalMinute;
    }
}
