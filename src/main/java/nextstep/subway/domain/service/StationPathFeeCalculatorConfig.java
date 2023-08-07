package nextstep.subway.domain.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class StationPathFeeCalculatorConfig {
    private final static BigDecimal FEE_PER_DISTANCE_UNIT = BigDecimal.valueOf(100);

    @Bean
    public AbstractStationPathFeeCalculator secondSectionPathFeeCalculator() {
        return SectionPathFeeCalculator.builder()
                .startPoint(BigDecimal.valueOf(51))
                .feeIncreasedDistanceUnit(BigDecimal.valueOf(8))
                .feePerDistanceUnit(FEE_PER_DISTANCE_UNIT)
                .build();
    }

    @Bean
    public AbstractStationPathFeeCalculator firstSectionPathFeeCalculator(AbstractStationPathFeeCalculator secondSectionPathFeeCalculator) {
        return SectionPathFeeCalculator.builder()
                .startPoint(BigDecimal.TEN)
                .endPoint(BigDecimal.valueOf(50))
                .feeIncreasedDistanceUnit(BigDecimal.valueOf(5))
                .feePerDistanceUnit(FEE_PER_DISTANCE_UNIT)
                .nextPathFeeCalculator(secondSectionPathFeeCalculator)
                .build();
    }
}
