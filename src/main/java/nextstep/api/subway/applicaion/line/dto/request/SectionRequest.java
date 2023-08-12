package nextstep.api.subway.applicaion.line.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public SectionRequest(final Long upStationId, final Long downStationId, final int distance, final int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
