package nextstep.subway.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StationPathResponse {
    private BigDecimal distance;
    private Long duration;
    private BigDecimal fee;
    private List<StationResponse> stations;
}
