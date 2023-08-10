package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.fixture.MemberSpec;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.service.*;
import nextstep.subway.unit.fixture.StationLineSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
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

    @DisplayName("청소년 할인 적용된 요금 계산")
    @Test
    void calculateFeeTest_Teenager_DiscountFee() {
        //given
        final AbstractStationPathFeeCalculator userAgeDiscountFeeCalculator = UserAgeDiscountFeeCalculator.builder()
                .build();

        final Member member = MemberSpec.of("TEST@nextstep.com", 18);

        final StationPathFeeContext context = StationPathFeeContext.builder()
                .distance(BigDecimal.valueOf(8))
                .lines(Collections.emptyList())
                .member(member)
                .build();

        //when
        final BigDecimal fee = userAgeDiscountFeeCalculator.calculateFee(BigDecimal.valueOf(1250), context);

        //then
        final BigDecimal twentyPercent = BigDecimal.valueOf(20).divide(BigDecimal.valueOf(100), 1, RoundingMode.DOWN);
        final BigDecimal expectedFee = BigDecimal.valueOf(-1250 + 350).multiply(twentyPercent);

        Assertions.assertEquals(0, fee.compareTo(expectedFee));
    }

    @DisplayName("어린이 할인 적용된 요금 계산")
    @Test
    void calculateFeeTest_Children_DiscountFee() {
        //given
        final AbstractStationPathFeeCalculator userAgeDiscountFeeCalculator = UserAgeDiscountFeeCalculator.builder()
                .build();

        final Member member = MemberSpec.of("TEST@nextstep.com", 6);

        final StationPathFeeContext context = StationPathFeeContext.builder()
                .distance(BigDecimal.valueOf(8))
                .lines(Collections.emptyList())
                .member(member)
                .build();

        //when
        final BigDecimal fee = userAgeDiscountFeeCalculator.calculateFee(BigDecimal.valueOf(1250), context);

        //then
        final BigDecimal fiftyPercent = BigDecimal.valueOf(50).divide(BigDecimal.valueOf(100), 1, RoundingMode.DOWN);
        final BigDecimal expectedFee = BigDecimal.valueOf(-1250 + 350).multiply(fiftyPercent);

        Assertions.assertEquals(0, fee.compareTo(expectedFee));
    }

}