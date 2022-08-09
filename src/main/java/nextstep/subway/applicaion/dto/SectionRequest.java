package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
