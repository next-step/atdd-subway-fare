package nextstep.subway.domain.service.fee;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class SectionPathFeeCalculator extends AbstractStationPathFeeCalculator {
    private final BigDecimal startPoint;
    private final BigDecimal endPoint;
    private final BigDecimal feeIncreasedDistanceUnit;
    private final BigDecimal feePerDistanceUnit;

    @Builder
    public SectionPathFeeCalculator(AbstractStationPathFeeCalculator nextPathFeeCalculator, BigDecimal startPoint, BigDecimal endPoint, BigDecimal feeIncreasedDistanceUnit, BigDecimal feePerDistanceUnit) {
        super(nextPathFeeCalculator);

        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.feeIncreasedDistanceUnit = feeIncreasedDistanceUnit;
        this.feePerDistanceUnit = feePerDistanceUnit;
    }

    @Override
    public BigDecimal calculate(StationPathFeeContext context) {
        final BigDecimal distance = context.getDistance();
        if (distance.compareTo(startPoint) < 0) {
            return BigDecimal.ZERO;
        }

        final BigDecimal additionalFee = calculateSectionFee(distance);
        if (Objects.isNull(endPoint)) {
            return additionalFee;
        }

        return additionalFee.min(calculateMaximumFee());
    }

    private BigDecimal calculateMaximumFee() {
        return calculateSectionFee(endPoint.subtract(BigDecimal.ONE));
    }

    private BigDecimal calculateSectionFee(BigDecimal currentPoint) {
        return currentPoint.subtract(startPoint)
                .divide(feeIncreasedDistanceUnit, 0, RoundingMode.DOWN)
                .add(BigDecimal.ONE)
                .multiply(feePerDistanceUnit);
    }
}
