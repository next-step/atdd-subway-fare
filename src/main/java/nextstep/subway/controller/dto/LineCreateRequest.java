package nextstep.subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class LineCreateRequest {
    private String name;
    private String color;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    private long distance;
    private long duration;
    private long extraFare;

    public LineCreateRequest() {
    }

    public List<Long> stationIds(){
        return List.of(upStationId, downStationId);
    }

}
