package nextstep.core.subway.line.application.dto;

public class LineRequest {
    private final String name;

    private final String color;

    private final Long upStationId;

    private final Long downStationId;

    private final int distance;

    private final int duration;

    private final int additionalFare;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration, int additionalFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }
}
