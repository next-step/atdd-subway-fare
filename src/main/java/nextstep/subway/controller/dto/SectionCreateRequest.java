package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class SectionCreateRequest {
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @Min(1)
    private Long distance;
    @Min(1)
    private Long duration;

    public SectionCreateRequest(Long upStationId, Long downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Long> stationIds(){
        return List.of(upStationId, downStationId);
    }

}
