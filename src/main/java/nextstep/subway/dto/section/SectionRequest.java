package nextstep.subway.dto.section;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;
    private Integer duration;

    protected SectionRequest() {}

    public SectionRequest(Long downStationId, Long upStationId, Integer distance, Integer duration) {
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
