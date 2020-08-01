package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.map.domain.SubwayPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DistanceProportionalFarePolicyTest {

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        subwayPath = mock(SubwayPath.class);
    }

    @DisplayName("기본 요금을 계산한다.")
    @Test
    void calculate() {
        // given
        DistanceProportionalFarePolicy fareCalculator = new DistanceProportionalFarePolicy();
        when(subwayPath.calculateDistance()).thenReturn(10);
        FareContext fareContext = new FareContext(subwayPath);

        // when
        fareCalculator.calculate(fareContext);

        // then
        Assertions.assertThat(fareContext.getFare().getValue()).isEqualTo(1250);
    }

    @DisplayName("10km초과∼50km까지(5km마다 100원)")
    @ParameterizedTest
    @CsvSource({"13, 1350", "50, 2050"})
    public void calculateForOverFare(int distance, int result) {
        when(subwayPath.calculateDistance()).thenReturn(distance);
        FareContext fareContext = new FareContext(subwayPath);
        DistanceProportionalFarePolicy policy = new DistanceProportionalFarePolicy();

        // when
        policy.calculate(fareContext);

        // then
        Assertions.assertThat(fareContext.getFare().getValue()).isEqualTo(result);
    }

    @DisplayName("50km초과 시 (8km마다 100원)")
    @ParameterizedTest
    @CsvSource({"57, 2150", "66, 2250"})
    public void calculateForExtraOverFare(int distance, int result) {
        when(subwayPath.calculateDistance()).thenReturn(distance);
        FareContext fareContext = new FareContext(subwayPath);
        DistanceProportionalFarePolicy policy = new DistanceProportionalFarePolicy();

        // when
        policy.calculate(fareContext);

        // then
        Assertions.assertThat(fareContext.getFare().getValue()).isEqualTo(result);
    }
}