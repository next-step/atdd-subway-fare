package nextstep.subway.application.dto.request;


import lombok.Getter;

@Getter
public class AddSectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;
    private final Long duration;

    public AddSectionRequest(Long upStationId, Long downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
