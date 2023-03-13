package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private int additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
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
