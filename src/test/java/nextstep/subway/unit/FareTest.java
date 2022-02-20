package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("10km 이하 기본요금")
    @Test
    void calculateFare() {
        // given
        Fare fare = new Fare(10);

        // when
        int fareCal = fare.calculateOverFare();

        // then
        assertThat(fareCal).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 시 -> 15km 100원 추가 요금")
    @Test
    void calculateOverFare15() {
        // given
        Fare fare = new Fare(15);

        // when
        int fareCalOver = fare.calculateOverFare();

        // then
        assertThat(fareCalOver).isEqualTo(1_350);
    }

    @DisplayName("10km 초과 시 -> 16km 200원 추가 요금")
    @Test
    void calculateOverFare16() {
        // given
        Fare fare = new Fare(16);

        // when
        int fareCalOver = fare.calculateOverFare();

        // then
        assertThat(fareCalOver).isEqualTo(1_450);
    }

    @DisplayName("10km 초과 시 -> 50km 800원 추가 요금")
    @Test
    void calculateOverFare50() {
        // given
        Fare fare = new Fare(50);

        // when
        int fareCalOver = fare.calculateOverFare();

        // then
        assertThat(fareCalOver).isEqualTo(2_050);
    }

    @DisplayName("50km 초과 시 -> 58km 100원 추가 요금")
    @Test
    void calculateOverFare58() {
        // given
        Fare fare = new Fare(58);

        // when
        int fareCalOver = fare.calculateOverFare();

        // then
        assertThat(fareCalOver).isEqualTo(2_150);
    }

    @DisplayName("50km 초과 시 -> 59km 200원 추가 요금")
    @Test
    void calculateOverFare59() {
        // given
        Fare fare = new Fare(59);

        // when
        int fareCalOver = fare.calculateOverFare();

        // then
        assertThat(fareCalOver).isEqualTo(2_250);
    }
}
