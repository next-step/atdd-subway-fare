package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    private SectionRequest() {
    }

    private SectionRequest(Long upStationId, Long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public static SectionRequest of(Long upStationId, Long downStationId, int distance, int duration) {
        return new SectionRequest(upStationId, downStationId, distance, duration);
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
