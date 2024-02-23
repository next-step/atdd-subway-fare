package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class SectionCreateRequest {

    @NotNull
    private Long downStationId;
    @NotNull
    private Long upStationId;
    @Min(1)
    private long distance;
    @Min(1)
    private long duration;


    public SectionCreateRequest() {
    }

    public SectionCreateRequest(Long downStationId, Long upStationId, long distance, long duration) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Long> stationIds(){
        return List.of(upStationId, downStationId);
    }

}
