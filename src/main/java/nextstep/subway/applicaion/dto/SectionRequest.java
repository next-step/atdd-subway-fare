package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

import static nextstep.utils.NumberUtils.requirePositiveNumber;

@Getter
@NoArgsConstructor
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    @Positive
    private int distance;
    @Positive
    private int duration;

    public SectionRequest(Long upStationId, Long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = requirePositiveNumber(distance);
        this.duration = requirePositiveNumber(duration);
    }


}
