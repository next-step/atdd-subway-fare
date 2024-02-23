package nextstep.line.application.dto;

import nextstep.line.exception.CreateRequestNotValidException;

import java.util.Objects;

public class SectionCreateRequest {
    private Long downStationId;
    private Long upStationId;
    private int distance;
    private int duration;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(final Long upStationId, final Long downStationId, final int distance, final int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void validate() {
        if (Objects.isNull(upStationId)) {
            throw new CreateRequestNotValidException("upStationId can not be null");
        }
        if (Objects.isNull(downStationId)) {
            throw new CreateRequestNotValidException("downStationId can not be null");
        }
        if (downStationId.equals(upStationId)) {
            throw new CreateRequestNotValidException("upStationId and downStationId can not be the same");
        }
        if (distance <= 0) {
            throw new CreateRequestNotValidException("distance must be greater than 0");
        }
        if (duration <= 0) {
            throw new CreateRequestNotValidException("duration must be greater than 0");
        }
    }
}
