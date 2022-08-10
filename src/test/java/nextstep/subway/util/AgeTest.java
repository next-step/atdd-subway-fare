package nextstep.subway.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @DisplayName("나이의 경계 값 테스트")
    @ParameterizedTest
    @MethodSource("ageBoundary")
    void ageBoundaryTest(int userAge, Age age) {
        Age findUserAge = AgeFactory.findUsersAge(userAge);

        assertThat(findUserAge).isExactlyInstanceOf(age.getClass());
    }

    private static Stream<Arguments> ageBoundary() {
        return Stream.of(
                Arguments.of(6, new Children()),
                Arguments.of(12, new Children()),
                Arguments.of(13, new Teenager()),
                Arguments.of(18, new Teenager()),
                Arguments.of(19, new Adult())
        );
    }
}