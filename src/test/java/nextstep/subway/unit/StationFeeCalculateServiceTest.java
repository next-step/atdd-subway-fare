package nextstep.subway.unit;

import nextstep.subway.domain.service.StationFeeCalculateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class StationFeeCalculateServiceTest {

    private final StationFeeCalculateService stationFeeCalculateService = new StationFeeCalculateService();

    @DisplayName("거리에 따른 기본 요금 계산")
    @Test
    void calculateFeeTest_NormalFee() {
        //given
        final BigDecimal distance = BigDecimal.valueOf(9);

        //when
        final BigDecimal fee = stationFeeCalculateService.calculateFee(distance);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250)));
    }

    @DisplayName("10KM<=..<=50KM 구간 요금 계산")
    @Test
    void calculateFeeTest_MORE_THAN10KM_LESS_THAN_50KM() {
        //given
        final BigDecimal distance = BigDecimal.valueOf(24);

        //when
        final BigDecimal fee = stationFeeCalculateService.calculateFee(distance);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250 + 300)));
    }

    @DisplayName("50KM> 구간 요금 계산")
    @Test
    void calculateFeeTest_LARGE_THEN_50KM() {
        //given
        final BigDecimal distance = BigDecimal.valueOf(67);

        //when
        final BigDecimal fee = stationFeeCalculateService.calculateFee(distance);

        //then
        Assertions.assertEquals(0, fee.compareTo(BigDecimal.valueOf(1250 + 800 + 300)));
    }
}