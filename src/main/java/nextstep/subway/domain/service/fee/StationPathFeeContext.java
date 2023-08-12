package nextstep.subway.domain.service.fee;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.StationLine;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class StationPathFeeContext {
    private BigDecimal distance;
    private List<StationLine> lines;
}
