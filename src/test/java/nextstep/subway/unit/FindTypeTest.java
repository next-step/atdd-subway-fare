package nextstep.subway.unit;

import nextstep.subway.domain.FindType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FindTypeTest {
    @DisplayName("DISTANCE, DURATION 타입의 객체 생성")
    @ParameterizedTest
    @MethodSource(value = "provideType")
    void from(String typeString, FindType findType) {
        // when
        FindType type = FindType.from(typeString);

        // then
        assertThat(type).isEqualTo(findType);
    }

    private static Stream<Arguments> provideType() {
        return Stream.of(
                Arguments.of("DISTANCE", FindType.DISTANCE),
                Arguments.of("DURATION", FindType.DURATION)
        );
    }

    @DisplayName("isDistance메서드 테스트")
    @Test
    void isDistance() {
        // when & then
        assertThat(FindType.DISTANCE.isDistance()).isTrue();
        assertThat(FindType.DISTANCE.isDuration()).isFalse();
    }

    @DisplayName("isDuration메서드 테스트")
    @Test
    void isDuration() {
        // when & then
        assertThat(FindType.DURATION.isDuration()).isTrue();
        assertThat(FindType.DURATION.isDistance()).isFalse();
    }
}
