package nextstep.line.application.request;

public class SectionAddRequest {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer duration;

    public SectionAddRequest(Long upStationId, Long downStationId, Integer distance, Integer duration) {
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
