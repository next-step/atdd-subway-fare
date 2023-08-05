package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum StationFeePolicyBySection {
    SECTION_51_KM(BigDecimal.valueOf(51), BigDecimal.valueOf(8), null),
    SECTION_10_KM(BigDecimal.TEN, BigDecimal.valueOf(5), SECTION_51_KM);

    public static final BigDecimal feePerDistanceUnit = BigDecimal.valueOf(100);

    private final BigDecimal boundaryDistance;
    private final BigDecimal feeIncreasedDistanceUnit;
    private final StationFeePolicyBySection nextPolicy;

    public BigDecimal calculateSectionFee(BigDecimal distance) {
        if (distance.compareTo(boundaryDistance) < 0) {
            return BigDecimal.ZERO;
        }

        final BigDecimal sectionFee = distance.subtract(boundaryDistance)
                .divide(feeIncreasedDistanceUnit, 0, RoundingMode.DOWN)
                .add(BigDecimal.ONE)
                .multiply(feePerDistanceUnit);

        return calculateMaximumSectionFee()
                .map(sectionFee::min)
                .orElse(sectionFee);
    }

    private Optional<BigDecimal> calculateMaximumSectionFee() {
        return Optional.ofNullable(nextPolicy)
                .map(StationFeePolicyBySection::getBoundaryDistance)
                .map(nextSectionBoundaryDistance -> nextSectionBoundaryDistance.subtract(boundaryDistance))
                .map(sectionDistance -> sectionDistance.divide(feeIncreasedDistanceUnit, 0, RoundingMode.DOWN))
                .map(feePerDistanceUnit::multiply);
    }
}
