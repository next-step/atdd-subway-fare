package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산 테스트")
public class FareCalculatorTest {

    private FareCalculatorFactory fareCalculator  = new FareCalculatorFactory();

    @ParameterizedTest
    @DisplayName("가격 덧셈")
    @MethodSource("provideCalculateParameter")
    public void fareCalculatorTest(Fare fare, Distance distance, Age age, Fare expectedFare) {
        // given
        FareSpecification specification = new FareSpecification(age);
        FareCalculation calculator = FareCalculatorFactory.getFareCalculator(specification);

        // when
        Fare result = calculator.calculate(fare, distance);

        // then
        assertThat(result).isEqualTo(expectedFare);
    }

    private static Stream< Arguments > provideCalculateParameter() {
        return Stream.of(
                Arguments.of(Fare.of(100), Distance.of(5), Age.of(-1), Fare.of(1350)),
                Arguments.of(Fare.of(300), Distance.of(20), Age.of(20), Fare.of(1850)),
                Arguments.of(Fare.of(1000), Distance.of(51), Age.of(100), Fare.of(3250))
        );
    }
}
