package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

@DisplayName("거리별 요금 정책")
class FareDistanceStrategyTest {

    static Stream<Arguments> under50Fare() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(12, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(30, 1650),
                Arguments.of(40, 1850),
                Arguments.of(50, 2050)
        );
    }

    static Stream<Arguments> over50Fare() {
        return Stream.of(
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(66, 2250),
                Arguments.of(67, 2350),
                Arguments.of(74, 2350),
                Arguments.of(75, 2450)
        );
    }

    private FareDistanceStrategy strategy = new FareDistanceStrategy();

    @DisplayName("10km 이하의 경로는 기본요금을 반환 한다")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ParameterizedTest
    void basic(int distance) {
        // when
        BigDecimal fare = strategy.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(BigDecimal.valueOf(1250));
    }

    @DisplayName("10~50km 범위는 기본 요금에 5Km 마다 100원을 추가한다")
    @MethodSource("under50Fare")
    @ParameterizedTest
    void under_50Km(int distance, int addFare) {
        // when
        BigDecimal fare = strategy.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(BigDecimal.valueOf(addFare));
    }


    @DisplayName("50km 이상의 범위는 이전 정책을 적용 후, 8km 마다 100원을 추가한다")
    @MethodSource("over50Fare")
    @ParameterizedTest
    void over_50Km(int distance, int addFare) {
        // when
        BigDecimal fare = strategy.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(BigDecimal.valueOf(addFare));
    }

}