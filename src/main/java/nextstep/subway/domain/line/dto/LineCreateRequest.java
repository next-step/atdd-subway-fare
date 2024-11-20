package nextstep.subway.domain.line.dto;

public class LineCreateRequest {
    private String name;
    private String color;
    private Long upwardStationId;
    private Long downwardStationId;
    private int distance;
    private int transitTime;
    private Long additionalFare;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, Long upwardStationId, Long downwardStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
        this.additionalFare = 0L;
    }

    public LineCreateRequest(String name, String color, Long upwardStationId, Long downwardStationId, int distance, int transitTime) {
        this.name = name;
        this.color = color;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
        this.transitTime = transitTime;
        this.additionalFare = 0L;
    }

    public LineCreateRequest(String name, String color, Long upwardStationId, Long downwardStationId, int distance, int transitTime, Long additionalFare) {
        this.name = name;
        this.color = color;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
        this.transitTime = transitTime;
        this.additionalFare = additionalFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getTransitTime() {
        return transitTime;
    }

    public Long getAdditionalFare() {
        return additionalFare;
    }
}
