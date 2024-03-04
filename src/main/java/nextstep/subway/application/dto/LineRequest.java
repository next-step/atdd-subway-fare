package nextstep.subway.application.dto;

public class LineRequest {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    private int additionalFee;

    public Long getId() {
        return id;
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

    public int getAdditionalFee() {
        return additionalFee;
    }
}
