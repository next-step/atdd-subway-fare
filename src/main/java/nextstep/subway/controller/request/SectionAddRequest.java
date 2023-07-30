package nextstep.subway.controller.request;

import nextstep.subway.domain.command.SectionAddCommand;

public class SectionAddRequest implements SectionAddCommand {

    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Integer duration;

    public SectionAddRequest() {
    }

    public SectionAddRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
