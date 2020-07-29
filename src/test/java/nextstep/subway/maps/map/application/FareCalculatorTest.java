package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new FareCalculator();
    }

    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */
    @DisplayName("지하철 운임을 정책에 맞게 계산한다. ")
    @ParameterizedTest
    @CsvSource({"5,1250", "13, 1350", "18,1450", "50,2050", "57,2150", "63,2250"})
    void calculate(int distance, int expectedFare) {
        //when
        int fare = fareCalculator.calculate(distance);
        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     * <p>
     * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
     * ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
     * ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
     * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
     * ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
     */
    @DisplayName("지하철 운임을 정책에 맞게 계산한다. ")
    @ParameterizedTest
    @CsvSource({"5,0,1250", "13, 100, 1450", "18,100, 1550", "50,1000,3050", "57,0,2150"})
    void calculateWithExtraFare(int distance, int extraFare, int expectedFare) {
        //given
        SubwayPath subwayPath = mock(SubwayPath.class);
        given(subwayPath.calculateDistance()).willReturn(distance);
        given(subwayPath.calculateMaxLineExtraFare()).willReturn(Money.wons(extraFare));

        //when
        Money fare = fareCalculator.calculate(subwayPath);
        //then
        assertThat(fare).isEqualTo(Money.wons(expectedFare));
    }


}