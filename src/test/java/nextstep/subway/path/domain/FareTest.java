package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {


    @ParameterizedTest
    @MethodSource("fareProvider")
    void calculateOverFare(int distance, int fare) {
        FareCalculationStrategy strategy = FareCalculationStrategyFactory.of(distance);
        assertThat(new Fare(strategy).get()).isEqualTo(fare);
    }

    private static Stream<Arguments> fareProvider() {
        return Stream.of(
                Arguments.of(8, 1250),
                Arguments.of(13, 1350),
                Arguments.of(20, 1450),
                Arguments.of(50, 2050),
                Arguments.of(52, 2250)
        );
    }






}
