package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalTime;
import java.util.function.Supplier;

import static java.time.format.DateTimeFormatter.ISO_TIME;

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

    public LineRequest(String name, String color, String startTime, String endTime, int intervalTime, int additionalFare, Long upStationId, Long downStationId, int distance, int duration) {
        this.name = name;
        this.color = color;
        if (startTime != null) {
            this.startTime = LocalTime.parse(startTime, ISO_TIME);
        }
        if (endTime != null) {
            this.endTime = LocalTime.parse(endTime, ISO_TIME);
        }
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

    public Supplier<Line> getEntitySupplier() {
        return () -> Line.builder()
                .name(this.name)
                .color(this.color)
                .additionalFare(this.additionalFare)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .intervalTime(this.intervalTime)
                .build();
    }
}
