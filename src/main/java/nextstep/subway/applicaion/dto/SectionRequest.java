package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this(upStationId, downStationId, distance, 0 );
    }
}
