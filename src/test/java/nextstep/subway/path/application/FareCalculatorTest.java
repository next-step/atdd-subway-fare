package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FareCalculatorTest {

    @Mock
    private PathResult pathResult;

    @ParameterizedTest
    @CsvSource(value = {"7:1250", "45:1950", "50:2050", "66:2250"},  delimiter = ':')
    void calculate(int distance, int expectedFare) {
        // given
        when(pathResult.getTotalDistance()).thenReturn(distance);
        FareCalculator fareCalculator = new FareCalculator();

        // when
        Fare fare = fareCalculator.calculate(pathResult);

        // then
        assertThat(fare.getFareValue()).isEqualTo(expectedFare);
    }
}
