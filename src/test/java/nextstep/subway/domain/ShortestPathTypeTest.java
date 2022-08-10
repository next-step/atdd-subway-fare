package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static nextstep.subway.domain.ShortestPathType.DISTANCE;
import static nextstep.subway.domain.ShortestPathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ShortestPathTypeTest {

    @DisplayName("String type 을 전달받아 ShortestPathType 생성 확인")
    @ParameterizedTest
    @MethodSource
    void shortestPathTypeFactoryTest(String input, ShortestPathType expected) {
        assertThat(ShortestPathType.from(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> shortestPathTypeFactoryTest() {
        return Stream.of(
                Arguments.of("distance", DISTANCE),
                Arguments.of("duration", DURATION)
        );
    }

    @DisplayName("String type 이 잘못되었을 때 에러가 발생")
    @ParameterizedTest
    @ValueSource(strings = "unknown")
    @NullSource
    void shortestPathTypeFactoryExceptionTest(String input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> ShortestPathType.from(input));
    }

}