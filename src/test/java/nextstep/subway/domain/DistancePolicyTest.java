package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DistancePolicyTest {

    @ParameterizedTest(name = "최소 거리가 {0}일때 거리에 따른 요금은 {1}원이다.")
    @MethodSource("generateData")
    void calculateFare(int distance, int fare) {
        //when
        int result = DistanceFarePolicy.calculate(distance);

        //then
        assertThat(result).isEqualTo(fare);
    }

    static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(0, 1_250),
                Arguments.of(9, 1_250),
                Arguments.of(10, 1_350),
                Arguments.of(11, 1_350),
                Arguments.of(16, 1_450),
                Arguments.of(49, 2_050),
                Arguments.of(50, 2_150),
                Arguments.of(51, 2_150),
                Arguments.of(100, 2_750)
        );
    }
}