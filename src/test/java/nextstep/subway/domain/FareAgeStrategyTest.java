package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

@DisplayName("나이별 할인 정책")
class FareAgeStrategyTest {

    private FareAgeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FareAgeStrategy();
    }

    static Stream<Arguments> kidsFare() {
        return Stream.of(
                Arguments.of(1000, 6, 675),
                Arguments.of(1500, 6, 925),
                Arguments.of(1000, 10, 675),
                Arguments.of(1500, 10, 925),
                Arguments.of(1000, 12, 675),
                Arguments.of(1500, 12, 925)
        );
    }

    static Stream<Arguments> teenFare() {
        return Stream.of(
                Arguments.of(1000, 13, 870),
                Arguments.of(1500, 13, 1270),
                Arguments.of(1000, 15, 870),
                Arguments.of(1500, 15, 1270),
                Arguments.of(1000, 18, 870),
                Arguments.of(1500, 18, 1270)
        );
    }

    @DisplayName("할인 대상이 아닌 요금 계산")
    @Test
    void default_fare() {
        Fare 기본요금 = Fare.of(BigDecimal.valueOf(1_000));

        // when
        Fare fare = strategy.calculate(20, 기본요금);

        // then
        Assertions.assertThat(fare).isEqualTo(기본요금);
    }

    @DisplayName("어린이 요금 할인 계산")
    @MethodSource("kidsFare")
    @ParameterizedTest
    void kids(int originFare, int age, int compare) {
        // when
        Fare fare = strategy.calculate(age, Fare.of(BigDecimal.valueOf(originFare)));

        // then
        Assertions.assertThat(fare.getFare()).isEqualTo(BigDecimal.valueOf(compare));
    }

    @DisplayName("청소년 요금 할인 계산")
    @MethodSource("teenFare")
    @ParameterizedTest
    void teen(int originFare, int age, int compare) {
        // when
        Fare fare = strategy.calculate(age, Fare.of(BigDecimal.valueOf(originFare)));

        // then
        Assertions.assertThat(fare.getFare()).isEqualTo(BigDecimal.valueOf(compare));
    }

}