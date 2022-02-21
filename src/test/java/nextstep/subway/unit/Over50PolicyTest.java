package nextstep.subway.unit;

import nextstep.subway.domain.Over50Policy;
import nextstep.subway.domain.SubwayFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Over50PolicyTest {
    @DisplayName("10km~50km 구간 추가 요금 계산")
    @ParameterizedTest
    @MethodSource(value = "provideDistanceAndFare")
    void apply(int distance, int fare) {
        // given
        int defaultFare = 1250;
        SubwayFarePolicy policy = new Over50Policy();

        // when & then
        assertThat(policy.apply(defaultFare, distance)).isEqualTo(fare);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1250),
                Arguments.of(50, 1250),
                Arguments.of(51, 1350),
                Arguments.of(58, 1350),
                Arguments.of(59, 1450)
        );
    }
}
