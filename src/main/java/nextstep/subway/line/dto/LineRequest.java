package nextstep.subway.line.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int charge;

    public LineRequest() {}

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration, int charge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.charge = charge;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        this(name, color, upStationId, downStationId, distance, duration, 0);
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

    public int getCharge() {
        return charge;
    }
}
