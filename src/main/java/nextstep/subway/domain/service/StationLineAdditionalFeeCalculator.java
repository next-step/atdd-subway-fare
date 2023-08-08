package nextstep.subway.domain.service;

import lombok.Builder;

import java.math.BigDecimal;

public class StationLineAdditionalFeeCalculator extends AbstractStationPathFeeCalculator{
    @Builder
    public StationLineAdditionalFeeCalculator() {
    }

    @Override
    public BigDecimal calculate(StationPathFeeContext context) {
        return BigDecimal.valueOf(900);
    }
}
