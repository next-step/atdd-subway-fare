package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AgeTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("주어진 연령대로 해당 타입 찾기")
    void findAgeTypeByAgeParameter(final Age actual, final Age expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> findAgeTypeByAgeParameter() {
        return Stream.of(
            Arguments.of(Age.findAge(5), Age.BABY),
            Arguments.of(Age.findAge(12), Age.CHILD),
            Arguments.of(Age.findAge(19), Age.TEENAGER),
            Arguments.of(Age.findAge(20), Age.ADULT)
        );
    }

    @MethodSource
    @ParameterizedTest
    void discountFare(final int actual, final int expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> discountFare() {
        final int fare = 1250;
        return Stream.of(
            Arguments.of(Age.BABY.ageFare(fare), 0),
            Arguments.of(Age.CHILD.ageFare(fare), 800),
            Arguments.of(Age.TEENAGER.ageFare(fare), 1070),
            Arguments.of(Age.ADULT.ageFare(fare), 1250)
        );
    }

}