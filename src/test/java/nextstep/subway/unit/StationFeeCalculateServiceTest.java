package nextstep.subway.unit;

import nextstep.subway.domain.service.AbstractStationPathFeeCalculator;
import nextstep.subway.domain.service.SectionPathFeeCalculator;
import nextstep.subway.domain.service.StationPathFeeContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class StationFeeCalculateServiceTest {

    @DisplayName("10KM<=..<=50KM 구간 요금 계산")
    @Test
    void calculateFeeTest_MORE_THAN_10KM_LESS_THAN_50KM() {
        final AbstractStationPathFeeCalculator sectionPathFeeCalculator = SectionPathFeeCalculator.builder()
                .startPoint(BigDecimal.TEN)
                .endPoint(BigDecimal.valueOf(50))
                .feeIncreasedDistanceUnit(BigDecimal.valueOf(5))
                .feePerDistanceUnit(BigDecimal.valueOf(100))
                .build();

        //given
        final BigDecimal baseFee = BigDecimal.valueOf(1250);
        final BigDecimal distance = BigDecimal.valueOf(24);
        final StationPathFeeContext context = StationPathFeeContext.builder()
                .distance(distance)
                .build();

        //when
        final BigDecimal fee = sectionPathFeeCalculator.calculateFee(baseFee, context);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250 + 300)));
    }

    @DisplayName("50KM> 구간 요금 계산")
    @Test
    void calculateFeeTest_LARGE_THEN_50KM() {
        final AbstractStationPathFeeCalculator secondSectionPathFeeCalculator = SectionPathFeeCalculator.builder()
                .startPoint(BigDecimal.valueOf(51))
                .feeIncreasedDistanceUnit(BigDecimal.valueOf(8))
                .feePerDistanceUnit(BigDecimal.valueOf(100))
                .build();

        final AbstractStationPathFeeCalculator firstSectionPathFeeCalculator = SectionPathFeeCalculator.builder()
                .startPoint(BigDecimal.TEN)
                .endPoint(BigDecimal.valueOf(50))
                .feeIncreasedDistanceUnit(BigDecimal.valueOf(5))
                .feePerDistanceUnit(BigDecimal.valueOf(100))
                .nextPathFeeCalculator(secondSectionPathFeeCalculator)
                .build();

        //given
        final BigDecimal baseFee = BigDecimal.valueOf(1250);
        final BigDecimal distance = BigDecimal.valueOf(67);
        final StationPathFeeContext context = StationPathFeeContext.builder()
                .distance(distance)
                .build();

        //when
        final BigDecimal fee = firstSectionPathFeeCalculator.calculateFee(baseFee, context);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250 + 800 + 300)));
    }
}