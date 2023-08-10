package nextstep.subway.section.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer duration;

    private SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Integer distance, Integer duration) {
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
