package nextstep.subway.unit;

import nextstep.subway.domain.AgeDiscountSection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeDiscountSectionTest {
    @ParameterizedTest
    @MethodSource("provideAgeDiscountSection")
    @DisplayName("연령 구간 찾기")
    void find(int age, AgeDiscountSection expected) {
        assertThat(AgeDiscountSection.find(age).get()).isEqualTo(expected);
    }

    public static Stream<Arguments> provideAgeDiscountSection() {
        return Stream.of(
                Arguments.of(6, AgeDiscountSection.CHILD),
                Arguments.of(12, AgeDiscountSection.CHILD),
                Arguments.of(13, AgeDiscountSection.TEENAGER),
                Arguments.of(18, AgeDiscountSection.TEENAGER)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 19})
    @DisplayName("연령 구간 없는 경우")
    void notFind(int age) {
        assertThat(AgeDiscountSection.find(age).isEmpty()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideCalculateFare")
    @DisplayName("연령 구간별 할인요금 계산")
    void calculate(AgeDiscountSection ageDiscountSection, int fare, int expected) {
        assertThat(ageDiscountSection.calculate(fare)).isEqualTo(expected);
    }

    public static Stream<Arguments> provideCalculateFare() {
        return Stream.of(
                Arguments.of(AgeDiscountSection.CHILD, 1350, 500),
                Arguments.of(AgeDiscountSection.TEENAGER, 1350, 800)
        );
    }
}
