package nextstep.subway.controller.request;


import nextstep.subway.domain.command.LineCreateCommand;

public class LineCreateRequest implements LineCreateCommand {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Integer duration;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, Long distance, Integer duration) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public Long getUpStationId() {
        return upStationId;
    }

    @Override
    public Long getDownStationId() {
        return downStationId;
    }

    @Override
    public Long getDistance() {
        return distance;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }
}
