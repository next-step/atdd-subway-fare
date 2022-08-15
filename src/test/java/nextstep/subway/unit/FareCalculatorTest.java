package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FareCalculatorTest {

    private Path path;

    @BeforeEach
    void setUp() {
        path = mock(Path.class);
    }

    @DisplayName("기본 구간 요금 (1km ~ 10km)")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void defaultFare(int distance) {
        when(path.extractDistance()).thenReturn(distance);

        var calulator = new FareCalculator();

        assertThat(calulator.getFare(path)).isEqualTo(1250);
    }

    @DisplayName("추가 구간 요금 (11 ~ 50km)")
    @ParameterizedTest
    @CsvSource(value = {
            "11, 1350",
            "15, 1350",
            "16, 1450",
            "50, 2050"
    })
    void firstRangeFare(int distance, int expectedFare) {
        when(path.extractDistance()).thenReturn(distance);

        var calulator = new FareCalculator();

        assertThat(calulator.getFare(path)).isEqualTo(expectedFare);
    }

    @DisplayName("추가 구간 요금 (51km ~ )")
    @ParameterizedTest
    @CsvSource(value = {
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350"
    })
    void secondRangeFare(int distance, int expectedFare) {
        when(path.extractDistance()).thenReturn(distance);

        var calulator = new FareCalculator();

        assertThat(calulator.getFare(path)).isEqualTo(expectedFare);
    }
}
