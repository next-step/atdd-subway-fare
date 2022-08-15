package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.fare.FareCalculatorChain;
import nextstep.subway.domain.fare.SecondRangeFareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecondRangeFareCalculatorTest {
    private Path path;

    @BeforeEach
    void setUp() {
        path = mock(Path.class);

    }

    @DisplayName("추가 구간 요금 (51km ~ )")
    @ParameterizedTest
    @CsvSource(value = {
            "50, 0",
            "51, 100",
            "58, 100",
            "59, 200",
            "66, 200",
            "67, 300"
    })
    void firstRangeFare(int distance, int expectedFare) {
        when(path.extractDistance()).thenReturn(distance);
        FareCalculatorChain calculator = new SecondRangeFareCalculator();

        int fare = calculator.calculate(path, 0);

        assertThat(fare).isEqualTo(expectedFare);
    }
}
