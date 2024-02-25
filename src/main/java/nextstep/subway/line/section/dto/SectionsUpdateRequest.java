package nextstep.subway.line.section.dto;

public class SectionsUpdateRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;
    private Long duration;

    public SectionsUpdateRequest(Long downStationId,
                                 Long upStationId,
                                 Long distance,
                                 Long duration) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
