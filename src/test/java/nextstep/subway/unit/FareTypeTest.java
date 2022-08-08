package nextstep.subway.unit;

import nextstep.subway.domain.FareType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTypeTest {

    @DisplayName("각 요금에 따른 Fare 타입을 찾는다")
    @ParameterizedTest
    @MethodSource("findFare")
    public void find_fare(int distance, FareType type) {
        // when
        FareType fareType = FareType.calculateType(distance);

        // then
        assertThat(fareType).isEqualTo(type);
    }

    @DisplayName("0이하의 거리를 전달받는 경우 예외를 던진다")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void invalid_fare(int distance) {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> FareType.calculateType(distance);

        // then
        assertThatThrownBy(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수이여야만 합니다.");
    }

    private static Stream<Arguments> findFare() {
        return Stream.of(
                Arguments.of(10, FareType.BASIC_DISTANCE),
                Arguments.of(11, FareType.MIDDLE_DISTANCE),
                Arguments.of(50, FareType.MIDDLE_DISTANCE),
                Arguments.of(51, FareType.LONG_DISTANCE)
        );
    }
}
