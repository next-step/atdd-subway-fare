package nextstep.subway.domain.service;

import lombok.Builder;

import java.math.BigDecimal;

public class UserAgeDiscountFeeCalculator extends AbstractStationPathFeeCalculator {

    @Builder
    public UserAgeDiscountFeeCalculator(AbstractStationPathFeeCalculator nextPathFeeCalculator) {
        super(nextPathFeeCalculator);
    }

    @Override
    public BigDecimal calculate(StationPathFeeContext context) {

        return BigDecimal.ZERO;
    }
}
