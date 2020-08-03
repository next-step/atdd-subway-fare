package nextstep.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.map.domain.SubwayPath;

public class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
    }

    @DisplayName("지하철 운임을 정책에 맞게 계산한다.")
    @CsvSource({"10, 1250", "11, 1350", "50, 2050", "57, 2150", "63, 2250"})
    @ParameterizedTest
    void 지하철_운임을_정책에_맞게_계산한다(int distance, int expectedFare) {
        // given
        SubwayPath subwayPath = mock(SubwayPath.class);
        given(subwayPath.calculateDistance()).willReturn(distance);

        // when
        int fare = fareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("추가 요금이 있는 노선을 이용할 경우, 측정된 요금에 추가되어 계산한다.")
    @CsvSource({"5, 0, 1250", "13, 100, 1450", "18, 100, 1550", "50, 1000, 3050", "57, 0, 2150"})
    @ParameterizedTest
    void 지하철_운임을_추가요금_추가해서_계산한다(int distance, int extraFare, int expectedFareValue) {
        // given
        SubwayPath subwayPath = mock(SubwayPath.class);
        given(subwayPath.calculateDistance()).willReturn(distance);
        given(subwayPath.calculateMaxLineExtraFare()).willReturn(Money.drawNewMoney(extraFare));

        // when
        Money fare = fareCalculator.calculate(subwayPath);
        Money expectedFare = Money.drawNewMoney(expectedFareValue);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
