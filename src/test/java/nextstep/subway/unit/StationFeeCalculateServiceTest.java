package nextstep.subway.unit;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.service.fee.AbstractStationPathFeeCalculator;
import nextstep.subway.domain.service.fee.SectionPathFeeCalculator;
import nextstep.subway.domain.service.fee.StationLineAdditionalFeeCalculator;
import nextstep.subway.domain.service.fee.StationPathFeeContext;
import nextstep.subway.unit.fixture.StationLineSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static nextstep.utils.UnitTestUtils.createEntityTestIds;

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

    @DisplayName("추가 요금이 있는 노선의 요금계산")
    @Test
    void calculateFeeTest_Line_AdditionalFee() {
        //given
        final Station aStation = new Station("aStation");
        final Station bStation = new Station("bStation");
        final Station cStation = new Station("cStation");
        final Station dStation = new Station("dStation");

        createEntityTestIds(List.of(aStation, bStation, cStation, dStation), 1L);

        //1호선
        final BigDecimal line_1_additionalFee = BigDecimal.valueOf(600);
        final StationLine line_1 = StationLineSpec.of(aStation, bStation, BigDecimal.ONE, 1000L, line_1_additionalFee);

        //2호선
        final BigDecimal line_2_additionalFee = BigDecimal.valueOf(900);
        final StationLine line_2 = StationLineSpec.of(cStation, dStation, BigDecimal.TEN, 1000L, line_2_additionalFee);

        final AbstractStationPathFeeCalculator lineAdditionalFeeCalculator = StationLineAdditionalFeeCalculator.builder()
                .build();

        final BigDecimal baseFee = BigDecimal.valueOf(1250);
        final BigDecimal distance = BigDecimal.valueOf(5);
        final List<StationLine> lines = List.of(line_1, line_2);

        final StationPathFeeContext context = StationPathFeeContext.builder()
                .distance(distance)
                .lines(lines)
                .build();
        //when
        final BigDecimal fee = lineAdditionalFeeCalculator.calculateFee(baseFee, context);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250 + 900)));
    }

}