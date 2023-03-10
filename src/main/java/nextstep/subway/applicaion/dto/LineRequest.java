package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private int extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }

    public boolean isExistSectionInfo() {
        return upStationId != null && downStationId != null && distance != 0 && duration != 0;
    }
}
