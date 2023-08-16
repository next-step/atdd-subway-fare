package nextstep.line.application.request;

public class LineCreateRequest {

    private String name;
    private String color;
    private Integer surcharge;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer duration;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, Integer distance, Integer duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public LineCreateRequest(String name, String color, Integer surcharge, Long upStationId, Long downStationId, Integer distance, Integer duration) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
