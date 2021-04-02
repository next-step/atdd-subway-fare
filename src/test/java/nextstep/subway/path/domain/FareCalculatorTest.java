package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.NonLoginMember;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산 테스트")
@ExtendWith(MockitoExtension.class)
public class FareCalculatorTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    @ParameterizedTest
    @DisplayName("가격 덧셈")
    @MethodSource("provideCalculateParameter")
    public void fareCalculatorTest(int fare, int distance, int age, Fare expectedFare) {
        // given
        PathResult pathResult = mock(PathResult.class);
        when(pathResult.getLineMaxFare()).thenReturn(fare);
        when(pathResult.getTotalDistance()).thenReturn(distance);
        FareParameter parameter = FareParameter.of(pathResult, new LoginMember(1L, EMAIL, PASSWORD, age));

        // when
        FareCalculation calculator = FareCalculatorFactory.getFareCalculator(parameter);
        Fare result = calculator.calculate();

        // then
        assertThat(result).isEqualTo(expectedFare);
    }

    private static Stream< Arguments > provideCalculateParameter() {
        return Stream.of(
                Arguments.of(100, 5, 1, Fare.of(1350)),
                Arguments.of(100, 5, 6, Fare.of(500)),
                Arguments.of(100, 5, 18, Fare.of(800)),
                Arguments.of(300, 20, 20, Fare.of(1850)),
                Arguments.of(1000, 51, 100, Fare.of(3250))
        );
    }

    @ParameterizedTest
    @DisplayName("비로그인 사용자의 가격 덧셈")
    @MethodSource("provideAnonymousCalculateParameter")
    public void fareCalculatorAnonymousTest(int fare, int distance, Fare expectedFare) {
        // given
        PathResult pathResult = mock(PathResult.class);
        when(pathResult.getLineMaxFare()).thenReturn(fare);
        when(pathResult.getTotalDistance()).thenReturn(distance);
        FareParameter parameter = FareParameter.of(pathResult, new NonLoginMember());

        // when
        FareCalculation calculator = FareCalculatorFactory.getFareCalculator(parameter);
        Fare result = calculator.calculate();

        // then
        assertThat(result).isEqualTo(expectedFare);
    }

    private static Stream< Arguments > provideAnonymousCalculateParameter() {
        return Stream.of(
                Arguments.of(100, 5, Fare.of(1350)),
                Arguments.of(300, 20, Fare.of(1850)),
                Arguments.of(1000, 51, Fare.of(3250))
        );
    }
}
