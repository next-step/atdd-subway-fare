package nextstep.subway.unit;

import nextstep.subway.domain.FareType;
import nextstep.subway.domain.fare.BasicStrategy;
import nextstep.subway.domain.fare.FareStrategy;
import nextstep.subway.domain.fare.LongStrategy;
import nextstep.subway.domain.fare.MiddleStrategy;
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

    @DisplayName("각 요금에 따른 Fare 전략을 찾는다")
    @ParameterizedTest
    @MethodSource("findFare")
    public void find_fare(int distance, Class strategy) {
        // when
        FareStrategy fareStrategy = FareType.findStrategy(distance);

        // then
        assertThat(fareStrategy).isInstanceOf(strategy);
    }

    @DisplayName("0이하의 거리를 전달받는 경우 예외를 던진다")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void invalid_fare(int distance) {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> FareType.findStrategy(distance);

        // then
        assertThatThrownBy(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수이여야만 합니다.");
    }

    private static Stream<Arguments> findFare() {
        return Stream.of(
                Arguments.of(10, BasicStrategy.class),
                Arguments.of(11, MiddleStrategy.class),
                Arguments.of(50, MiddleStrategy.class),
                Arguments.of(51, LongStrategy.class)
        );
    }
}
