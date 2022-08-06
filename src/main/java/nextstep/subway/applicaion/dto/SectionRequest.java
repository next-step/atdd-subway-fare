package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int requiredTime;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance, int requiredTime) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.requiredTime = requiredTime;
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

    public int getRequiredTime() {
        return requiredTime;
    }
}
