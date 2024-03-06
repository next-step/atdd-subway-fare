package nextstep.line.ui;


import nextstep.line.domain.Color;

public class LineRequest {

    private String name;
    private Color color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int extraFare;

    protected LineRequest() {
    }

    public LineRequest(String name, Color color, Long upStationId, Long downStationId, int distance, int duration, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, Color color, Long upStationId, Long downStationId, int distance, int duration) {
        this(name, color, upStationId, downStationId, distance, duration, 0);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
