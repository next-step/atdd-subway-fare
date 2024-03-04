package nextstep.subway.dto.line;


public class LineRequest {
    private String name;
    private String color;
    private Integer distance;
    private Long upStationId;
    private Long downStationId;

    private Integer duration;

    protected LineRequest() {}

    public LineRequest(
        String name,
        String color,
        Integer distance,
        Long upStationId,
        Long downStationId,
        Integer duration
    ) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.duration = duration;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDuration() {
        return duration;
    }
}
