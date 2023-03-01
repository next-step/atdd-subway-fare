package nextstep.subway.unit;

import nextstep.subway.domain.fare.AgeFarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AgeFarePolicyTest {

    private AgeFarePolicy ageFarePolicy;

    @BeforeEach
    void setUp() {
        ageFarePolicy = new AgeFarePolicy();
    }

    @DisplayName("기본요금이 주어질 때 연령별 요금 할인 정책에 따라 최종 요금을 반환한다.")
    @MethodSource("ageFareProvider")
    @ParameterizedTest(name = "나이: {1}, 최종요금: {2}, {3}")
    void ageFare(int fare, int age, int expected, String message) {
        assertThat(ageFarePolicy.discountUnderTeenager(age, fare)).isEqualTo(expected);
    }

    private static Stream<Arguments> ageFareProvider() {
        return Stream.of(
                Arguments.of(1250, 5, 1250, "6세 미만은 할인 정책이 없다."),
                Arguments.of(1250, 6, (int) ((1250 - 350) * 0.5), "6세 이상, 13세 미만은 350원 공제한 금액에서 50%할인 받는다."),
                Arguments.of(1250, 12, (int) ((1250 - 350) * 0.5), "6세 이상, 13세 미만은 350원 공제한 금액에서 50%할인 받는다."),
                Arguments.of(1250, 13, (int) ((1250 - 350) * 0.8), "13세 이상, 19세 미만은 350원 공제한 금액에서 20%할인 받는다."),
                Arguments.of(1250, 18, (int) ((1250 - 350) * 0.8), "13세 이상, 19세 미만은 350원 공제한 금액에서 20%할인 받는다."),
                Arguments.of(1250, 19, 1250, "19세 이상은 할인 정책이 없다.")
        );
    }
}
