package nextstep.subway.unit;

import nextstep.subway.domain.fare.BasicStrategy;
import nextstep.subway.domain.fare.LongStrategy;
import nextstep.subway.domain.fare.MiddleStrategy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FareStrategyTest {

    @ParameterizedTest
    @MethodSource("basicFare")
    public void basic_fare_test(int distance, int fare) {
        // given
        BasicStrategy strategy = new BasicStrategy();

        // when
        int resultFare = strategy.calculate(distance);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> basicFare() {
        return Stream.of(
                Arguments.of(1, 1250),
                Arguments.of(10, 1250)
        );
    }

    @ParameterizedTest
    @MethodSource("basicFareWithAge")
    public void basic_fare_with_age_test(int distance, int age, int fare) {
        // given
        BasicStrategy strategy = new BasicStrategy();

        // when
        int resultFare = strategy.calculateWithAge(distance, age);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> basicFareWithAge() {
        return Stream.of(
                Arguments.of(1, 10, 800),
                Arguments.of(1, 13, 1070),
                Arguments.of(1, 19, 1250)
        );
    }

    @ParameterizedTest
    @MethodSource("middleFare")
    public void middle_fare_test(int distance, int fare) {
        // given
        MiddleStrategy strategy = new MiddleStrategy();

        // when
        int resultFare = strategy.calculate(distance);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> middleFare() {
        return Stream.of(
                Arguments.of(11, 1350),
                Arguments.of(50, 2050)
        );
    }

    @ParameterizedTest
    @MethodSource("middleFareWithAge")
    public void middle_fare_with_age_test(int distance, int age, int fare) {
        // given
        MiddleStrategy strategy = new MiddleStrategy();

        // when
        int resultFare = strategy.calculateWithAge(distance, age);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> middleFareWithAge() {
        return Stream.of(
                Arguments.of(11, 10, 850),
                Arguments.of(11, 13, 1150),
                Arguments.of(11, 19, 1350)
        );
    }

    @ParameterizedTest
    @MethodSource("longFare")
    public void long_fare_test(int distance, int fare) {
        // given
        LongStrategy strategy = new LongStrategy();

        // when
        int resultFare = strategy.calculate(distance);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> longFare() {
        return Stream.of(
                Arguments.of(51, 2150),
                Arguments.of(60, 2250)
        );
    }

    @ParameterizedTest
    @MethodSource("longFareWithAge")
    public void long_fare_with_age_test(int distance, int age, int fare) {
        // given
        LongStrategy strategy = new LongStrategy();

        // when
        int resultFare = strategy.calculateWithAge(distance, age);

        // then
        assertThat(resultFare).isEqualTo(fare);
    }

    private static Stream<Arguments> longFareWithAge() {
        return Stream.of(
                Arguments.of(51, 10, 1250),
                Arguments.of(51, 13, 1790),
                Arguments.of(51, 19, 2150)
        );
    }
}
