package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @Test
    @DisplayName("최단 거리가 10km 이하일 경우 - 5km")
    void getFareOf5km() {
        // when
        int fare = FareCalculator.getFare(5);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("최단 거리가 10km 이하일 경우 - 10km")
    void getFareOf10km() {
        // when
        int fare = FareCalculator.getFare(10);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("최단 거리가 10~50km일 경우 - 23km")
    void getFareOf23km() {
        // when
        int fare = FareCalculator.getFare(23);

        // then
        assertThat(fare).isEqualTo(1550);
    }

    @Test
    @DisplayName("최단 거리가 10~50km일 경우 - 50km")
    void getFareOf50km() {
        // when
        int fare = FareCalculator.getFare(50);

        // then
        assertThat(fare).isEqualTo(2050);
    }

    @Test
    @DisplayName("최단 거리가 50km 초과일 경우 - 58km")
    void getFareOf58km() {
        // when
        int fare = FareCalculator.getFare(58);

        // then
        assertThat(fare).isEqualTo(2150);
    }

    @Test
    @DisplayName("최단 거리가 50km 초과일 경우 - 70km")
    void getFareOf70km() {
        // when
        int fare = FareCalculator.getFare(70);

        // then
        assertThat(fare).isEqualTo(2350);
    }
}
