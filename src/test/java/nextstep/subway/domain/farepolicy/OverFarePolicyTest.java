package nextstep.subway.domain.farepolicy;

import static nextstep.subway.domain.farepolicy.FarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OverFarePolicyTest {

    @DisplayName("경로조회 9km 의 추가금액은 0")
    @Test
    void calculateOverFare_1() {
        Policy farePolicy = new OverFarePolicy(9);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("경로조회 10km 의 추가금액은 0")
    @Test
    void calculateOverFare_2() {
        Policy farePolicy = new OverFarePolicy(10);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("경로조회 12km 의 추가금액은 100")
    @Test
    void calculateOverFare_3() {
        Policy farePolicy = new OverFarePolicy(12);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("경로조회 15km 의 추가금액은 100")
    @Test
    void calculateOverFare_4() {
        Policy farePolicy = new OverFarePolicy(15);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("경로조회 16km 의 추가금액은 200")
    @Test
    void calculateOverFare_5() {
        Policy farePolicy = new OverFarePolicy(16);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 200);
    }

    @DisplayName("경로조회 50km 의 추가금액은 800")
    @Test
    void calculateOverFare_6() {
        Policy farePolicy = new OverFarePolicy(50);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("경로조회 58km 의 추가금액은 900")
    @Test
    void calculateOverFare_7() {
        Policy farePolicy = new OverFarePolicy(58);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 900);
    }

    @DisplayName("경로조회 59km 의 추가금액은 1000")
    @Test
    void calculateOverFare_8() {
        Policy farePolicy = new OverFarePolicy(59);

        int overFare = farePolicy.calculate(DEFAULT_FARE);

        assertThat(overFare).isEqualTo(DEFAULT_FARE + 1000);
    }

}