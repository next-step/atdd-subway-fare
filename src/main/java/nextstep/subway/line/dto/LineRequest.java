package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {

    public static final int DEFAULT_EXTRA_CHARGE = 0;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int extraCharge;

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.extraCharge = DEFAULT_EXTRA_CHARGE;
    }

    public void addExtraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
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

    public int getExtraCharge() {
        return extraCharge;
    }

    public Line toLine() {
        return new Line(name, color, extraCharge);
    }
}
