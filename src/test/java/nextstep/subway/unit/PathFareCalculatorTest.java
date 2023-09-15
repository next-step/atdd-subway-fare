package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.utils.path.PathFareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 추가 요금 계산 단위 테스트")
public class PathFareCalculatorTest {

    private final PathFareCalculator calculator = new PathFareCalculator();

    @Test
    @DisplayName("[성공] 기본 운임 요금 계산")
    void 기본_운임_요금_계산() {
        // When
        Integer fare = calculator.calculate(5L);

        // Then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("[성공] 이용 거리 10km")
    void 이용_거리_10km() {
        // When
        Integer fare = calculator.calculate(10L);

        // Then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("[성공] 이용 거리 10km 초과")
    void 이용_거리_10km_초과() {
        // When
        Integer fare = calculator.calculate(16L);

        // Then
        assertThat(fare).isEqualTo(1450);
    }

    @Test
    @DisplayName("[성공] 이용 거리 50km")
    void 이용_거리_50km() {
        // When
        Integer fare = calculator.calculate(50L);

        // Then
        assertThat(fare).isEqualTo(2050);
    }

    @Test
    @DisplayName("[성공] 이용 거리 51km")
    void 이용_거리_51km() {
        // When
        Integer fare = calculator.calculate(51L);

        // Then
        assertThat(fare).isEqualTo(2150);
    }

    @Test
    @DisplayName("[성공] 이용 거리 58km")
    void 이용_거리_58km() {
        // When
        Integer fare = calculator.calculate(58L);

        // Then
        assertThat(fare).isEqualTo(2150);
    }

    @Test
    @DisplayName("[성공] 이용 거리 59km")
    void 이용_거리_59km() {
        // When
        Integer fare = calculator.calculate(59L);

        // Then
        assertThat(fare).isEqualTo(2250);
    }
}
