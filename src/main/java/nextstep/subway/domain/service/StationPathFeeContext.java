package nextstep.subway.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class StationPathFeeContext {
    private BigDecimal distance;
}
