package nextstep.subway.section.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Long duration;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
