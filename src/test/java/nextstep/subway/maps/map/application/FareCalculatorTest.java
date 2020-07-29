package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
     */
    @DisplayName("지하철 운임을 정책에 맞게 계산한다. ")
    @ParameterizedTest
    @CsvSource({"5,1250", "13, 1350", "18,1450", "50,2050", "57,2150", "63,2250"})
    void calculateWithExtraFare(int distance, int expectedFare) {
        //given
        SubwayPath subwayPath = mock(SubwayPath.class);
//        subwayPath.getLineStationEdges().stream().map(LineStationEdge::)
        given(subwayPath.calculateDistance()).willReturn(distance);

        //when
        int fare = fareCalculator.calculate(distance);
        //then
        assertThat(fare).isEqualTo(expectedFare);
    }


}