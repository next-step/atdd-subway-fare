package nextstep.subway.domain.service.fee;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class AbstractStationPathFeeCalculator {
    public AbstractStationPathFeeCalculator() {
    }

    public AbstractStationPathFeeCalculator(AbstractStationPathFeeCalculator nextPathFeeCalculator) {
        this.nextPathFeeCalculator = nextPathFeeCalculator;
    }

    private AbstractStationPathFeeCalculator nextPathFeeCalculator;

    protected abstract BigDecimal calculate(StationPathFeeContext context);

    public BigDecimal calculateFee(BigDecimal baseFee, StationPathFeeContext context) {
        final BigDecimal resultFee = baseFee.add(calculate(context));

        if (Objects.nonNull(nextPathFeeCalculator)) {
            return nextPathFeeCalculator.calculateFee(resultFee, context);
        }

        return resultFee;
    }
}
