package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fare.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 관련 테스트")
class FareTest {

    @ParameterizedTest(name = "총 거리가 {0}km일 경우, {1}의 요금이 발생한다")
    @MethodSource("farePerDistance")
    void getFareAt10(int distance, int expected) {
        // when
        Fare fare = new Fare(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}세가 총 거리 {1}km를 탈 경우, {2}의 요금이 발생한다")
    @MethodSource("farePerAge")
    void getFareForChild(int age, int distance, int expected) {
        // when
        Fare fare = new Fare(distance, age);

        // then
        assertThat(fare.getFare()).isEqualTo(expected);
    }

    private static Stream<Arguments> farePerDistance() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(50, 2050),
                Arguments.of(57, 2150),
                Arguments.of(58, 2150)
        );
    }

    private static Stream<Arguments> farePerAge() {
        return Stream.of(
                Arguments.of(6, 10, 450),
                Arguments.of(12, 10, 450),
                Arguments.of(13, 10, 720),
                Arguments.of(18, 10, 720),
                Arguments.of(20, 10, 1250)
        );
    }


}