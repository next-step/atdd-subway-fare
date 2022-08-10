package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int price;
    private int distance;
    private int duration;

    private LineRequest() { }

    private LineRequest(String name, String color, Long upStationId, Long downStationId, int price, int distance, int duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.price = price;
        this.distance = distance;
        this.duration = duration;
    }

    public static LineRequest of(String name, String color, Long upStationId, Long downStationId, int price, int distance, int duration) {
        return new LineRequest(name, color, upStationId, downStationId, price, distance, duration);
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

    public int getPrice() {
        return price;
    }
}
