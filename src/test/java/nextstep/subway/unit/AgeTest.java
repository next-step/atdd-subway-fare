package nextstep.subway.unit;

import nextstep.subway.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("주어진 연령대로 해당 타입 찾기")
    void getAgeTypeByAgeParameter(final Age actual, final Age expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> getAgeTypeByAgeParameter() {
        return Stream.of(
                Arguments.of(Age.getAge(5), Age.BABY),
                Arguments.of(Age.getAge(12), Age.CHILD),
                Arguments.of(Age.getAge(19), Age.TEEN),
                Arguments.of(Age.getAge(20), Age.ADULT)
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
                Arguments.of(Age.BABY.applyDiscounts(fare), 0),
                Arguments.of(Age.CHILD.applyDiscounts(fare), 800),
                Arguments.of(Age.TEEN.applyDiscounts(fare), 1070),
                Arguments.of(Age.ADULT.applyDiscounts(fare), 1250)
        );
    }

}
