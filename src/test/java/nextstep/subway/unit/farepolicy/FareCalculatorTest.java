package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFareRange;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("경로 결과 테스트")
@ExtendWith(MockitoExtension.class)
public class FareCalculatorTest {
    @Mock
    private Path path;

    @CsvSource({
        "9,1250",
        "10,1250",
        "11,1350",
        "50,2150",
        "51,2250",
        "59,2350"
    })
    @DisplayName("요금 계산 테스트")
    @ParameterizedTest
    void calculate(int distance, int totalCost) {
        // Given
        when(path.extractDistance()).thenReturn(distance);

        // When, Then
        FareCalculator calculator = new FareCalculator();
        assertThat(calculator.calculate(path)).isEqualTo(totalCost);
    }
}
