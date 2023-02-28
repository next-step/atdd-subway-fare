package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.math.BigDecimal;

public class LineRequest {
    private String name;
    private String color;
    private BigDecimal extraFare;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public Line toEntity() {
        if (this.extraFare == null) {
            new Line(this.name, this.color);
        }
        return new Line(this.name, this.color, this.extraFare);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getExtraFare() {
        return extraFare;
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
}
