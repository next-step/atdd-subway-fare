package nextstep.subway.applicaion.dto;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public static LineRequest of(String name, String color,
                                 long upStationId, long downStationId,
                                 int distance, int duration) {
        LineRequest lineRequest = new LineRequest();
        lineRequest.name = name;
        lineRequest.color = color;
        lineRequest.upStationId = upStationId;
        lineRequest.downStationId = downStationId;
        lineRequest.distance = distance;
        lineRequest.duration = duration;

        return lineRequest;
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

}
