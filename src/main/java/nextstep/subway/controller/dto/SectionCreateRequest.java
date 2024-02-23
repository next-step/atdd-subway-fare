package nextstep.subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
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

    public List<Long> stationIds(){
        return List.of(upStationId, downStationId);
    }

}
