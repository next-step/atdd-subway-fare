package nextstep.core.subway.section.application.dto;

public class SectionRequest {

    private Long upStationId;

    private Long downStationId;

    private int distance;

    private Long lineId;

    private int duration;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance, int duration, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.lineId = lineId;
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public static SectionRequest mergeForCreateLine(Long stationLineId, SectionRequest request) {
        return new SectionRequest(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance(),
                request.getDuration(),
                stationLineId
        );
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

    public Long getLineId() {
        return lineId;
    }

    public int getDuration() {
        return duration;
    }
}
