package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.fare.FirstRangeFareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirstRangeFareCalculatorTest {

    private Path path;

    @BeforeEach
    void setUp() {
        path = mock(Path.class);

    }

    @DisplayName("추가 구간 요금 (11 ~ 50km)")
    @ParameterizedTest
    @CsvSource(value = {
            "10, 0",
            "11, 100",
            "15, 100",
            "16, 200",
            "50, 800"
    })
    void firstRangeFare(int distance, int expectedFare) {
        when(path.extractDistance()).thenReturn(distance);
        var calculator = new FirstRangeFareCalculator();
        var fare = calculator.calculate(path, 0);

        assertThat(fare).isEqualTo(expectedFare);
    }
}
