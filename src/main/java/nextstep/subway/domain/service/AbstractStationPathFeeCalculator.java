package nextstep.subway.domain.service;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public abstract class AbstractStationPathFeeCalculator {
    public AbstractStationPathFeeCalculator() {
    }

    public AbstractStationPathFeeCalculator(AbstractStationPathFeeCalculator nextPathFeeCalculator) {
        this.nextPathFeeCalculator = nextPathFeeCalculator;
    }

    private AbstractStationPathFeeCalculator nextPathFeeCalculator;

    public abstract BigDecimal calculate(StationPathFeeContext context);

    public BigDecimal calculateFee(BigDecimal baseFee, StationPathFeeContext context) {
        final BigDecimal resultFee = baseFee.add(calculate(context));

        log.info("result fee:"+resultFee);
        if (Objects.nonNull(nextPathFeeCalculator)) {
            return nextPathFeeCalculator.calculateFee(resultFee, context);
        }

        return resultFee;
    }
}
