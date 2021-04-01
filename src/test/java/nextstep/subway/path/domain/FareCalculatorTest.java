package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
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
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    private FareCalculatorFactory fareCalculator  = new FareCalculatorFactory();

    @ParameterizedTest
    @DisplayName("가격 덧셈")
    @MethodSource("provideCalculateParameter")
    public void fareCalculatorTest(Fare fare, Distance distance, int age, Fare expectedFare) {
        // given
        LoginMember member = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        FareSpecification specification = new FareSpecification(member);
        FareCalculation calculator = FareCalculatorFactory.getFareCalculator(specification);

        // when
        Fare result = calculator.calculate(fare, distance);

        // then
        assertThat(result).isEqualTo(expectedFare);
    }

    private static Stream< Arguments > provideCalculateParameter() {
        return Stream.of(
                Arguments.of(Fare.of(100), Distance.of(5), 0, Fare.of(1350)),
                Arguments.of(Fare.of(300), Distance.of(20), 20, Fare.of(1850)),
                Arguments.of(Fare.of(1000), Distance.of(51), 100, Fare.of(3250))
        );
    }

    @ParameterizedTest
    @DisplayName("비로그인 사용자의 가격 덧셈")
    @MethodSource("provideAnonymousCalculateParameter")
    public void fareCalculatorAnonymousTest(Fare fare, Distance distance, Fare expectedFare) {
        // given
        FareSpecification specification = new FareSpecification(null);
        FareCalculation calculator = FareCalculatorFactory.getFareCalculator(specification);

        // when
        Fare result = calculator.calculate(fare, distance);

        // then
        assertThat(result).isEqualTo(expectedFare);
    }

    private static Stream< Arguments > provideAnonymousCalculateParameter() {
        return Stream.of(
                Arguments.of(Fare.of(100), Distance.of(5), Fare.of(1350)),
                Arguments.of(Fare.of(300), Distance.of(20), Fare.of(1850)),
                Arguments.of(Fare.of(1000), Distance.of(51), Fare.of(3250))
        );
    }
}
