package nextstep.subway.applicaion.dto;

import java.math.BigDecimal;

public class LineRequest {
    private String name;
    private String color;
    private BigDecimal fare;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getFare() {
        return fare;
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
