package nextstep.subway.application.dto;

public class SectionRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int duration;

    public SectionRequest(final Long upStationId, final Long downStationId, final int distance,
                          final int duration) {
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

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
