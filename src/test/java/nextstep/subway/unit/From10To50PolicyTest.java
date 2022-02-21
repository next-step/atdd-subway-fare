package nextstep.subway.unit;

import nextstep.subway.domain.From10To50Policy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class From10To50PolicyTest {
    @DisplayName("10km~50km 구간 추가 요금 계산")
    @ParameterizedTest
    @MethodSource(value = "provideDistanceAndFare")
    void apply(int distance, int fare) {
        // given
        int defaultFare = 1250;
        From10To50Policy policy = new From10To50Policy();

        // when & then
        assertThat(policy.apply(defaultFare, distance)).isEqualTo(fare);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(50, 2050),
                Arguments.of(51, 2050)
        );
    }
}
